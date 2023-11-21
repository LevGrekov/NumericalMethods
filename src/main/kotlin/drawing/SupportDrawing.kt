package drawing

import androidx.compose.ui.graphics.Color

fun darkenColor(color: Color, factor: Double): Color {
    val red = (color.red * (1 - factor)).toInt()
    val green = (color.green * (1 - factor)).toInt()
    val blue = (color.blue * (1 - factor)).toInt()

    return Color(red, green, blue)
}

fun darkenColor(argb: Int, factor: Double): Color {
    val alpha = argb ushr 24 and 0xFF
    val red = (argb ushr 16 and 0xFF).toFloat()
    val green = (argb ushr 8 and 0xFF).toFloat()
    val blue = (argb and 0xFF).toFloat()

    val newRed = (red * (1 - factor)).toInt().coerceIn(0, 255)
    val newGreen = (green * (1 - factor)).toInt().coerceIn(0, 255)
    val newBlue = (blue * (1 - factor)).toInt().coerceIn(0, 255)

    return Color(alpha, newRed, newGreen, newBlue)
}
