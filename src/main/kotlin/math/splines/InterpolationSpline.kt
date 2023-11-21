package math.splines

import math.polynomials.Polynomial
import java.util.*

data class SplineSegment(val inf: Double, val sup: Double, val polynomial: Polynomial)
abstract class InterpolationSpline(points: Map<Double, Double>) {

    val points = TreeMap(points)

    abstract val segments: List<SplineSegment>
    val n: Int
        get() = points.size -1

    operator fun invoke(x: Double): Double? {
        require(segments.isNotEmpty()) { "Segments list can't be empty." }
        val segment: SplineSegment? = segments.lastOrNull { x in it.inf..it.sup }
        return segment?.polynomial?.invoke(x)
    }
    override fun toString(): String = buildString {
        if (segments.isNotEmpty()){
            segments.forEach{
                append("[${it.inf},${it.sup}] -> S3 = ${it.polynomial}")
                append("\n")
            }
        }
        else append("NULL")
    }
}