package drawing.painters.functions

import androidx.compose.ui.graphics.Color
import drawing.convertation.Plane
import drawing.painters.Painter

interface FunctionPainter : Painter {
    val color: Color
    var plane: Plane?
    val strokeWidth: Float
}