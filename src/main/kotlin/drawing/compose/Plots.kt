package drawing.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
fun showPlot(
    functions: List<Triple<(Double) -> Double?, Map<Double,Double>?, Pair<Color,String>>>,
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double,
) {

    val cartesianPainter = CartesianPainter(showGrid = true)
    cartesianPainter.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)

    fun generatePainters(functions: List<Triple<(Double) -> Double?, Map<Double,Double>?, Pair<Color,String>>>): List<Triple<FunctionPainter, PointsPainter?, Pair<Boolean,String>>> {
        return functions.map { (function, points, colorName) ->
            val funkPainter = FunctionPainter(function, colorName.first)
            funkPainter.plane = cartesianPainter.plane

            val pointsPainter = points?.let {
                PointsPainter(it, darkenColor(colorName.first, 0.5), 2f)
            }
            Triple(funkPainter, pointsPainter, true to colorName.second)
        }
    }

    var painters by remember { mutableStateOf(generatePainters(functions)) }

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

                    painters.forEach { (funkPainter, pointsPainter, boolName) ->
                        funkPainter.plane?.width = size.width
                        funkPainter.plane?.height = size.height
                        if (boolName.first) {
                            funkPainter.paint(this)
                            pointsPainter?.let {
                                pointsPainter.plane?.width = size.width
                                pointsPainter.plane?.height = size.height
                                pointsPainter.paint(this)
                            }
                        }
                    }
                }
                Column(
                    Modifier.padding(top = 2.dp).fillMaxWidth()
                        .background(Color(1f, 1f, 1f, 0.9f))
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            painters.forEachIndexed { index, (functionPainter, pointsPainter, boolName) ->
                                Switch(
                                    checked = boolName.first,
                                    onCheckedChange = {
                                        painters = painters.toMutableList().apply {
                                            this[index] = Triple(functionPainter, pointsPainter, it to boolName.second)
                                        }
                                    },
                                    modifier = Modifier.padding(2.dp),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = functionPainter.color
                                    )
                                )
                                Text(
                                    text = boolName.second,
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



