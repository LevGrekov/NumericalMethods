package math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ulp

fun Double.eq(other: Double, eps: Double) = abs(this - other) < eps

infix fun Double.eq(other: Double) = abs(this - other) < max(ulp, other.ulp) * 10.0

fun Double.neq(other: Double, eps: Double) = !this.eq(other, eps)

infix fun Double.neq(other: Double) = !this.eq(other)

fun factorial(n: Int): Int {
    var result = 1
    for (i in 1..n) {
        result *= i
    }
    return result
}

fun calculateDerivativeAtPoint(
    f: (Double) -> Double,
    x: Double,
    h: Double = 1e-5): Double
        = (f(x + h) - f(x - h)) / (2 * h)
fun findMaxError(lowLim: Double, upLim: Double,f: (Double) -> Double?, g: (Double) -> Double?,epsilon: Double = 10e-6): Double {
    val a = min(lowLim,upLim)
    val b = max(lowLim,upLim)
    var x = a
    var maxError = Double.MIN_VALUE
    while (x <= b) {
        val fValue = f(x)
        val gValue = g(x)
        if(fValue!= null && gValue != null){
            val error = abs(fValue - gValue)
            if (error > maxError) {
                maxError = error
            }
        }

        x += epsilon
    }
    return maxError
}

fun sgn(x: Double): Double {
    return when {
        x eq 0.0 -> 0.0
        x < 0 -> -1.0
        else -> 1.0
    }
}
