package drawing.painters.functions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import drawing.convertation.Converter
import drawing.convertation.Plane

class UsualFunctionPainter(
    var function: (Double) -> Double?,
    override val color: Color,
    override val strokeWidth: Float = 2f
) : FunctionPainter {

    override var plane: Plane? = null
    override fun paint(scope: DrawScope) {
        plane?.let {plane ->

            for (i in 0 until plane.width.toInt()){
                val x1 = Converter.xScr2Crt(i.toFloat(), plane)
                val x2 = Converter.xScr2Crt((i+1).toFloat(), plane)

                val y1 = function(x1)
                val y2 = function(x2)

                y1?.let {
                    y2?.let{
                        scope.drawLine(
                            color,
                            Offset(i.toFloat(), Converter.yCrt2Scr(y1, plane)),
                            Offset((i+1).toFloat(), Converter.yCrt2Scr(y2, plane)),
                            strokeWidth
                        )
                    }
                }
            }
        }

    }
}