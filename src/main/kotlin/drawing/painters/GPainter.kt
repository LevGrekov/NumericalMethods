package drawing.painters

import drawing.painters.functions.FunctionPainter

data class GPainter(
    val funkPainter: FunctionPainter,
    val pointsPainter: PointsPainter? = null,
    val text: String? = null,
    var show: Boolean = true
)