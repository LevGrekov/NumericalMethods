package drawing.painters
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import drawing.convertation.Converter
import drawing.convertation.Plane
import math.neq
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

@OptIn(ExperimentalTextApi::class)
open class CartesianPainter(val showGrid: Boolean) : Painter {

    companion object{
        private val df =  DecimalFormat("#.##########")
        var AXIS_COLOR = Color.Black
        var GRID_COLOR = Color.LightGray
        var GRID_WIDTH = 0.7f
        var LABELS_COLOR = Color.Black
        var FONT_SIZE = 12.sp
        var FONT_WEIGHT = FontWeight.Medium
    }

    var plane: Plane? = null
    var textMeasurer: TextMeasurer? = null
    var step: Double = 1.0

    fun getLowerLim(xMin: Double, step: Double) : Double {
        val ll = step * floor(xMin / step)
        return  if(ll>xMin) ll else ll+step
    }
    fun getStepInnovation(delta: Double,firstEnter: Double = 28.0) : Double{
        val sgn = if((delta/firstEnter).toInt() > 0) 1.0 else -1.0
        var i = 0.0
        while(i!=sgn*100){
            if(delta/firstEnter in (2.0.pow(i - 1.0) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i)))
                return 2.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i) * 5.0.pow(i)) .. (2.0.pow(i+1) * 5.0.pow(i)))
                return 5.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i+1) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i+1)))
                return 10.0 * 10.0.pow(i)
            i+=sgn
        }
        return delta
    }

    override fun paint(scope: DrawScope) {
        plane?.let{
            val stepY = getStepInnovation(abs(it.yMax-it.yMin))
            val stepX = getStepInnovation(abs(it.xMax-it.xMin))
            step = if(abs(it.yMax-it.yMin) > abs(it.xMax-it.xMin) ) stepY else stepX
//            println("delta: ${ abs(it.xMax-it.xMin)} ${abs(it.yMax-it.yMin)}")
//            println("Шаг :$stepX $stepY")
            if(showGrid) drawGrid(scope, step, step)
            paintAxis(scope)
            paintLabels(scope,step,step)
        }
    }

    private fun paintLabels(scope: DrawScope,stepX: Double,stepY: Double) {

        plane?.let {
            var x = getLowerLim(it.xMin,stepX)
            var y = getLowerLim(it.yMin,stepY)

            while (x < it.xMax){
                if(x neq 0.0){
                    paintXLabels(scope,x)
                }
                x+=stepX
            }
            while (y < it.yMax){
                if(y neq 0.0){
                    paintYLabels(scope,y)
                }
                y+=stepY
            }
        }
    }

    private fun paintAxis(scope: DrawScope) {

        plane?.let {
            val x0 = it.x0.coerceIn(0f, it.width)
            val y0 = it.y0.coerceIn(0f, it.height)
            scope.apply {
                drawLine(
                    AXIS_COLOR,
                    Offset(0f, y0),
                    Offset(size.width, y0),
                    1f)
                drawLine(
                    AXIS_COLOR,
                    Offset(x0, 0f),
                    Offset(x0, size.height),
                    1f)
            }
        }
    }

    private fun drawGrid(scope: DrawScope, stepX: Double, stepY: Double) {
        plane?.let {
            var x = getLowerLim(it.xMin,stepX)
            while (x < it.xMax){
                val xPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    drawLine(
                        GRID_COLOR,
                        Offset(xPos, 0f),
                        Offset(xPos, size.height),
                        GRID_WIDTH
                    )
                }
                x+=stepX
            }

            var y = getLowerLim(it.yMin,stepY)
            while (y < it.yMax){
                val yPos = Converter.yCrt2Scr(y, it)
                scope.apply {
                    drawLine(
                        GRID_COLOR,
                        Offset(0f, yPos),
                        Offset(it.width, yPos),
                        GRID_WIDTH
                    )
                }
                y+=stepY
            }
        }
    }

    @OptIn(ExperimentalTextApi::class)
    fun paintYLabels(scope: DrawScope, value: Double) {
        scope.apply {
            textMeasurer?.let {
                val text = it.measure(
                    df.format(value),
                    TextStyle(color = LABELS_COLOR, fontSize = FONT_SIZE, fontWeight = FONT_WEIGHT)
                )
                plane?.let { plane ->
                    var x = Converter.xCrt2Scr(0.0, plane).coerceIn(0f, plane.width)
                    if(x==plane.width) x -= 34f else x += 17f
                    val y = Converter.yCrt2Scr(value, plane) - text.size.width / 2
                    drawText(text, topLeft = Offset(x, y))
                }
            }
        }
    }

    @OptIn(ExperimentalTextApi::class)
    fun paintXLabels(scope: DrawScope, value: Double){
        scope.apply{
            textMeasurer?.let {
                val text = it.measure(
                    df.format(value),
                    TextStyle(color = LABELS_COLOR, fontSize = FONT_SIZE, fontWeight = FONT_WEIGHT)
                )
                plane?.let { plane ->
                    var y = Converter.yCrt2Scr(0.0, plane).coerceIn(0f, plane.height)
                    if(y==plane.height) y -= 34f  else y += 17f

                    val x = Converter.xCrt2Scr(value, plane) - text.size.width / 2
                    drawText(text, topLeft = Offset(x, y))
                }
            }
        }
    }

}
