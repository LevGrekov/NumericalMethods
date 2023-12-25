package math.polynomials

import math.eq
import kotlin.math.abs

@Suppress("NAME_SHADOWING")
class LegendrePolynomial(degree: Int, ck: Double = 1.0) : Polynomial() {
    init {
        _coeffs.clear()
        val a =  Polynomial(mapOf(0 to -1.0,2 to 1.0)).pow(degree).derivative(degree) * ck
        _coeffs.putAll(a.coeffs)
    }
    fun crutchFindingRoots() : List<Double>{
        val epsilon = 1e-7
        var x = -1.0
        val roots :MutableList<Double> = mutableListOf()
        while (x <= 1.0) {
            if (abs(this(x)) < 1e-6 ) roots.add(x)
            //println(this(x))
            x += epsilon
        }
        return  roots.toList().sorted()
    }

    fun findRootInInterval(start: Double, end: Double, tolerance: Double): Double {
        var x0 = (start + end) / 2.0 // Начальное приближение метода бисекции
        var end = end
        var start = start
        while (abs(end - start) > tolerance) {
            // Проверяем, в какой половине интервала находится x0 и обновляем интервал
            if (this(x0) * this.derivative(1)(start) < 0) {
                end = x0
            } else {
                start = x0
            }

            // Применяем метод Ньютона для получения нового приближения
            x0 -= this(x0) / this.derivative(1)(x0)
        }

        return x0
    }

    companion object{
        fun mapInterval(t: Double, a: Double, b: Double): Double {
            if (a eq b) return a
            val (minValue, maxValue) = if (a < b) a to b else b to a
            return (minValue + maxValue)/2.0 + (maxValue-minValue) * t /2.0
        }
    }
}