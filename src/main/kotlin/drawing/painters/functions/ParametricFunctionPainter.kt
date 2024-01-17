package drawing.painters.functions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import drawing.convertation.Converter
import drawing.convertation.Plane

class ParametricFunctionPainter (
    val xFunction: (Double) -> Double?,
    val yFunction: (Double) -> Double?,
    override val color: Color,
    val tMin: Double = 0.0,
    val tMax: Double = 10.0,
    val dt: Double = 0.1,
    override val strokeWidth: Float = 2f,
) : FunctionPainter {

    override var plane: Plane? = null
    override fun paint(scope: DrawScope) {

        plane?.let { plane ->
            var (t, tMax) = if (tMin < tMax) tMin to tMax else tMax to tMin
            while (t < tMax) {

                val x1 = xFunction(t)
                val y1 = yFunction(t)
                val x2 = xFunction(t+dt)
                val y2 = yFunction(t+dt)

                if(x1 != null && x2 != null && y1 != null && y2 != null){
                    scope.drawLine(
                        color,
                        Offset(Converter.xCrt2Scr(x1, plane), Converter.yCrt2Scr(y1, plane)),
                        Offset(Converter.xCrt2Scr(x2, plane), Converter.yCrt2Scr(y2, plane)),
                        strokeWidth
                    )
                }
                t += dt
            }
        }
    }
}
