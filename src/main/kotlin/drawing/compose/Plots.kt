package drawing.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import drawing.convertation.Plane
import drawing.painters.CartesianPainter
import drawing.painters.GPainter
import kotlin.math.abs


@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class)
@Composable
fun showPlot(
    painters: List<GPainter>,
    xMin:Double = -20.0,
    xMax:Double = 20.0,
    yMin:Double = -20.0,
    yMax:Double = 20.0
) {
    var funksShows by remember { mutableStateOf(painters.map{it.show}) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    val cartesianPainter = CartesianPainter(showGrid = true)
    cartesianPainter.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)
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
                Canvas( Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.White)
                    .transformable(transformState)
                    .onPointerEvent(PointerEventType.Scroll) {
                        scale += it.changes.first().scrollDelta.y
                    },
                ) {
                    cartesianPainter.plane?.let {
                        it.width = size.width
                        it.height = size.height
                        var trueScale = maxOf(scale, 1f/ abs(scale)).toDouble()
                        if(scale == 0f) trueScale = 1.0
                        it.xMin = (xMin - offset.x/10.0) * trueScale
                        it.xMax = (xMax - offset.x/10.0) * trueScale
                        it.yMin = (yMin + offset.y/10.0) * trueScale
                        it.yMax = (yMax + offset.y/10.0) * trueScale
                    }
                    cartesianPainter.paint(this)
                    println(offset)
                    painters.forEachIndexed{index, painter ->
                        cartesianPainter.plane?.let {
                            painter.funkPainter.plane = cartesianPainter.plane
                            painter.funkPainter.plane?.width = it.width
                            painter.funkPainter.plane?.height = it.height

                            painter.pointsPainter?.let {pp ->
                                pp.plane = cartesianPainter.plane
                                pp.plane?.width = size.width
                                pp.plane?.height = size.height
                            }

                        }


                        println(funksShows[index])
                        if (funksShows[index]) {
                            painter.funkPainter.paint(this)
                            painter.pointsPainter?.paint(this)
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
                        painters.forEachIndexed { index, it ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Switch(
                                    checked = funksShows[index],
                                    onCheckedChange = { newBool -> funksShows = funksShows.toMutableList().apply { this[index] = newBool } },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = it.funkPainter.color
                                    )
                                )
                                Text(
                                    text = it.text ?: "",
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



