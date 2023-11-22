package drawing.painters
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine
import drawing.convertation.Converter
import drawing.convertation.Plane
import drawing.convertation.Stepic
import math.neq
import java.text.DecimalFormat

@OptIn(ExperimentalTextApi::class)
open class CartesianPainter(val showGrid: Boolean) : Painter {

    companion object{
        private val paint = Paint().apply {
            color = Color.Black.toArgb()
        }
        private val font = Font().apply {
            size = 24f
        }
        private val df =  DecimalFormat("#.##########")
        val AXIS_COLOR = Color.Black
        val GRID_COLOR = Color.LightGray
        const val GRID_WIDTH = 0.7f
        val LABELS_COLOR = Color.Black
        val FONT_SIZE = 12.sp
        val FONT_WEIGHT = FontWeight.Medium
    }

    var plane: Plane? = null
    var textMeasurer: TextMeasurer? = null

    override fun paint(scope: DrawScope) {
        plane?.let{
            val stepY = Stepic.getStepInnovation(it.deltaY)
            val stepX = Stepic.getStepInnovation(it.deltaX)
            if(showGrid) drawGrid(scope,5.0,stepX,stepY)
            paintAxis(scope)
            paintLabels(scope,stepX,stepY)
        }
    }

    private fun paintLabels(scope: DrawScope,stepX: Double,stepY: Double) {

        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
            var y = Stepic.getLowerLim(it.yMin,stepY)

            while (x < it.xMax){
                if(x neq 0.0){
                    paintXLabels(scope,x)
                    x+=stepX
                }
            }
            while (y < it.yMax){
                if(y neq 0.0){
                    paintYLabels(scope,y)
                    y+=stepY
                }
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

    private fun drawGrid(scope: DrawScope, inStep:Double,stepX:Double,stepY:Double) {
        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
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

            var y = Stepic.getLowerLim(it.yMin,stepY)
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
                    if(y==plane.height) y -= 34f else y += 17f

                    val x = Converter.xCrt2Scr(value, plane) - text.size.width / 2
                    drawText(text, topLeft = Offset(x, y))
                }
            }
        }
    }

}
