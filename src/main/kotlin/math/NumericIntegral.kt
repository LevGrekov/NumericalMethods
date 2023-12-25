package math

import math.polynomials.LagrangePolynomial
import math.polynomials.LegendrePolynomial
import kotlin.math.PI
import kotlin.math.cos

class NumericIntegral(
    private val function: (Double) -> Double,
    lowerBound: Double,
    upperBound: Double
) {
    private val a: Double
    private val b: Double

    init {
        this.a = minOf(lowerBound, upperBound)
        this.b = maxOf(lowerBound, upperBound)
    }
    fun calculateRectangleMethod(n: Int): Double {
        val h = (b - a) / n

        var result = 0.0

        for (i in 0 until n) {
            val xi = a + i * h
            result += function(xi)
        }

        result *= h
        return result
    }

    fun calculateTrapezoidalMethod(n: Int): Double {
        val h = (b - a) / n

        var result = 0.0

        for (i in 0 until n) {
            val xi = a + i * h
            val xNext = a + (i + 1) * h

            result += (function(xi) + function(xNext)) / 2
        }

        result *= h
        return result
    }

    fun calculateSimpsonMethod(n: Int): Double {
        val h = (b - a) / n
        var result = function(a) + function(b)

        for (i in 1 until n) {
            val x = a + i * h
            result += if (i % 2 == 0) 2 * function(x) else 4 * function(x)
        }

        return result * h / 3
    }
    /**
     * квадратурная формула Гаусса с rho = 1
     */
    fun gaussianMethodWithRo1(n: Int): Double{
        var result = 0.0
        val legendrePoly = LegendrePolynomial(n)
        val roots = OptimizationMethods.findRoots(-1.0, 1.0, legendrePoly::invoke, legendrePoly.derivative(1)::invoke)
        if (roots.size != legendrePoly.size) throw Exception("Корни Полинома Лежандра нашлись неверно")
        val mappedRoots = roots.map { LegendrePolynomial.mapInterval(it,a,b) }

        mappedRoots.forEach{ xk ->
            val Ak = LagrangePolynomial.createFundamentalPoly(mappedRoots,xk).rhimanIntegral(a,b)
            result += Ak * function(xk)
        }
        return result
    }

    /**
     * квадратурная формула Гаусса с rho = 1/sqrt(1-x^2)
     */
    fun gaussianMethodWithWithSpecialRo(n: Int): Double{
        var result = 0.0
        val roots: MutableList<Double> = mutableListOf()
        for (k in 1..n){
            val xk = cos((2.0 * k - 1) * PI/(2.0 * n))
            roots.add(xk)
        }
        val mappedRoots = roots.map { LegendrePolynomial.mapInterval(it,a,b) }

        mappedRoots.forEach{ xk ->
            val Ak = LagrangePolynomial.createFundamentalPoly(mappedRoots,xk).rhimanIntegral(a,b)
            result += Ak * function(xk)
        }
        return result
    }

    fun InterpolationMethod(n: Int): Double{
        val h = (b - a) / (n-1).toDouble()
        val pointsX = (0 until n).map { a + it * h }.toList()
        val points = pointsX.associateWith { function(it) }
        val poly = LagrangePolynomial(points)
        return poly.rhimanIntegral(a,b)
    }
}