package math.splines

import math.polynomials.Polynomial
import java.util.*
import kotlin.math.sqrt

data class SplineSegment(val inf: Double, val sup: Double, val polynomial: Polynomial)

open class SegmentedFunction() {
    protected val segments: MutableList<SplineSegment> = mutableListOf()
    constructor(sgmnts: List<SplineSegment>) : this() {
        segments.addAll(sgmnts)
    }

    operator fun invoke(x: Double): Double? {
        require(segments.isNotEmpty()) { "Segments list can't be empty." }
        val segment: SplineSegment? = segments.lastOrNull { x in it.inf..it.sup }
        return segment?.polynomial?.invoke(x)
    }

    override fun toString(): String = buildString {
        if (segments.isNotEmpty()) {
            segments.forEach {
                append("[${it.inf},${it.sup}] -> S3 = ${it.polynomial}")
                append("\n")
            }
        } else append("NULL")
    }

    fun findMaximum(function: (Double) -> Double, lowLim: Double, uplim: Double): Double? {
        val epsilon = 1e-5 // Точность
        var a = lowLim
        var b = uplim

        val goldenRatio = (sqrt(5.0) - 1) / 2
        var x1 = a + (1 - goldenRatio) * (b - a)
        var x2 = a + goldenRatio * (b - a)

        var f1 = function(x1)
        var f2 = function(x2)

        while (Math.abs(b - a) > epsilon) {
            if (f1 > f2) {
                b = x2
                x2 = x1
                f2 = f1
                x1 = a + (1 - goldenRatio) * (b - a)
                f1 = function(x1)
            } else {
                a = x1
                x1 = x2
                f1 = f2
                x2 = a + goldenRatio * (b - a)
                f2 = function(x2)
            }
        }

        return (a + b) / 2
    }
}
