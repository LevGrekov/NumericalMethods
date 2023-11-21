package drawing.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import drawing.convertation.Plane
import drawing.darkenColor
import drawing.painters.CartesianPainter
import drawing.painters.FunctionPainter
import drawing.painters.PointsPainter
import kotlin.math.roundToInt
import kotlin.reflect.KFunction1


@Composable
fun showPlot(functions: List<Triple<(Double) -> Double?, Map<Double,Double>?, Color>>, xMin:Double, xMax:Double, yMin:Double, yMax:Double,){

    val cartesianPainter = CartesianPainter(showGrid = true)
    cartesianPainter.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)


    val painters: MutableList<Triple<FunctionPainter, PointsPainter?, Boolean>> = mutableListOf()

    functions.forEach { (function, points, color) ->
        val funkPainter = FunctionPainter(function, color)
        funkPainter.plane = cartesianPainter.plane

        val pointsPainter = points?.let {
            PointsPainter(it, darkenColor(color, 0.5), 2f)
        }

        painters.add(Triple(funkPainter, pointsPainter, true))
    }

    MaterialTheme {
        Box(Modifier.background(Color.Blue).fillMaxSize()) {
            Column(
                Modifier.padding(2.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                Modifier
                        .border(2.dp, MaterialTheme.colors.primary)
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.White)
                        .clipToBounds()
                ) {
                    cartesianPainter.plane?.width = size.width
                    cartesianPainter.plane?.height = size.height
                    cartesianPainter.paint(this)

                    painters.forEach{ (funkPainter,pointsPainter,bool,) ->
                        funkPainter.plane?.width = size.width
                        funkPainter.plane?.height = size.height
                        funkPainter.paint(this)
                        pointsPainter?.let {
                            pointsPainter.plane?.width = size.width
                            pointsPainter.plane?.height = size.height
                            pointsPainter.paint(this)
                        }
                    }
                }
                Column(Modifier.padding(top = 2.dp).fillMaxWidth()
                    .background(Color(1f, 1f, 1f, 0.9f))
                ) {
                    Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row {
                            painters.forEachIndexed { index, (functionPainter, pointsPainter, bool) ->
                                Switch(
                                    checked = bool,
                                    onCheckedChange = { bool = it },
                                    modifier = Modifier.padding(2.dp),
                                    colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                                )
                                Text(
                                    text = "Точки $index",
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

