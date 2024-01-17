package drawing.convertation

import kotlin.math.abs

class Plane (
    private var _xMin: Double,
    private var _xMax: Double,
    private var _yMin: Double,
    private var _yMax: Double,
    var width: Float,
    var height: Float
){
    var xMin: Double
        get() = _xMin - dx
        set(value){ _xMin = value }

    var xMax: Double
        get() = _xMax + dx
        set(value){ _xMax = value }

    var yMin: Double
        get() = _yMin - dy
        set(value){ _yMin = value }

    var yMax: Double
        get() = _yMax + dy
        set(value){ _yMax = value }

    private val dx: Double
        get() = if (OXY < OWH) xLen * (OWH / OXY - 1.0) / 2.0 else 0.0

    private val dy: Double
        get() = if (OXY > OWH) yLen * ((1.0 / OWH) / (1.0 / OXY) - 1.0) / 2.0 else 0.0

    private val OXY: Double
        get() = xLen / yLen

    private val OWH: Double
        get() = width.toDouble() / height.toDouble()

    private val xLen: Double
        get() = _xMax - _xMin

    private val yLen: Double
        get() = _yMax - _yMin

    val xDen: Double
        get() = width/(xMax-xMin)

    val yDen: Double
        get() = height/(yMax-yMin)

    val x0 : Float
        get() = Converter.xCrt2Scr(0.0, this)
    val y0 : Float
        get() = Converter.yCrt2Scr(0.0, this)
}