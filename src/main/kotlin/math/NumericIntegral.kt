package math

import math.polynomials.LagrangePolynomial
import math.polynomials.LegendrePolynomial
import math.polynomials.NewtonPolynomial
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
    fun leftRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        var result = 0.0

        for (i in 0 until n) {
            val xi = a + i * h
            result += h * function(xi)
        }

        return result
    }

    fun rightRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        var result = 0.0

        for (i in 1..n) {
            val xi = a + i * h
            result += h * function(xi)
        }

        return result
    }

    fun middleRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        var result = 0.0

        for (i in 0 until n) {
            val xi = a + i * h
            val xiNext = a + (i + 1) * h
            val xiMid = (xi + xiNext) / 2
            result += h * function(xiMid)
        }

        return result
    }

    fun trapezoidalMethod(n: Int): Double {
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

    fun simpsonMethod(n: Int): Double {
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
        val mappedRoots = LegendrePolynomial.mapInterval(roots,a,b)

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
        val mappedRoots = LegendrePolynomial.mapInterval(roots,a,b)

        mappedRoots.forEach{ xk ->
            val Ak = LagrangePolynomial.createFundamentalPoly(mappedRoots,xk).rhimanIntegral(a,b)
            result += Ak * function(xk)
        }
        return result
    }

    fun interpolationMethod(n: Int): Double{
        val h = (b - a) / (n-1).toDouble()
        val pointsX = (0 until n).map { a + it * h }.toList()
        val points = pointsX.associateWith { function(it) }
        val poly = NewtonPolynomial(points)
        return poly.rhimanIntegral(a,b)
    }
}