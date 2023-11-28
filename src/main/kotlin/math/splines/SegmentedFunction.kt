package math.splines

import math.polynomials.Polynomial
import java.util.*
import kotlin.math.abs
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

}
