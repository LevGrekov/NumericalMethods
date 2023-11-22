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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
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


@OptIn(ExperimentalTextApi::class)
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

    fun generatePainters(functions: List<Triple<(Double) -> Double?, Map<Double,Double>?, Pair<Color,String>>>): List<Triple<FunctionPainter, PointsPainter, Pair<Boolean,String>>> {
        return functions.map { (function, points, colorNamePair) ->
            val funkPainter = FunctionPainter(function, colorNamePair.first)
            funkPainter.plane = cartesianPainter.plane

            val pointsPainter = if(points == null){
                PointsPainter(mapOf(), colorNamePair.first, 2f)
            }
            else{
                PointsPainter(points, colorNamePair.first, 2f)
            }
            pointsPainter.plane = cartesianPainter.plane

            Triple(funkPainter, pointsPainter, true to colorNamePair.second)
        }
    }

    var painters by remember { mutableStateOf(generatePainters(functions)) }

    cartesianPainter.textMeasurer = rememberTextMeasurer()

    MaterialTheme {
        Box(Modifier
            .background(Color.Black).
            fillMaxSize().
            border(2.dp, Color.Black)) {
            Column(
                Modifier.padding(1.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.White)
                        .clipToBounds()
                ) {
                    cartesianPainter.plane?.width = size.width
                    cartesianPainter.plane?.height = size.height
                    cartesianPainter.paint(this)

                    painters.forEach { (funkPainter, pointsPainter, boolNamePair) ->
                        funkPainter.plane?.width = size.width
                        funkPainter.plane?.height = size.height

                        pointsPainter.plane?.width = size.width
                        pointsPainter.plane?.height = size.height

                        if (boolNamePair.first) {
                            funkPainter.paint(this)
                            pointsPainter.paint(this)
                        }
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(1f, 1f, 1f, 0.9f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        painters.forEachIndexed { index, (functionPainter, pointsPainter, boolName) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Switch(
                                    checked = boolName.first,
                                    onCheckedChange = {
                                        painters = painters.toMutableList().apply {
                                            this[index] = Triple(functionPainter, pointsPainter, it to boolName.second)
                                        }
                                    },
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



