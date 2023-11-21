package drawing.painters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import drawing.convertation.Converter
import drawing.convertation.Plane

class PointsPainter(var points: Map<Double,Double>, val color: Color, val radius:Float) : Painter {

    var plane: Plane? = null

    override fun paint(scope: DrawScope) {
        if(points.isNotEmpty()){
            paintPoints(scope)
        }
    }

    private fun paintPoints(scope: DrawScope){
        plane?.let { plane ->
            points.forEach{
                val scrX = Converter.xCrt2Scr(it.key,plane)
                val scrY = Converter.yCrt2Scr(it.value,plane)

                if( scrX in radius..plane.width - radius &&
                    scrY in radius..plane.height - radius)

                    scope.apply {
                        drawCircle(color, radius, Offset(scrX,scrY))
                        drawCircle(color = Color.Black, radius, center = Offset(scrX, scrY), style = Stroke(1f))
                    }
            }
        }
    }

}