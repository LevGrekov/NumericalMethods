package math

import math.polynomials.LagrangePolynomial
import math.polynomials.LegendrePolynomial
import math.polynomials.NewtonPolynomial
import java.lang.StringBuilder
import kotlin.math.PI
import kotlin.math.cos

class NumericIntegral(
    private val function: (Double) -> Double,
    lowerBound: Double,
    upperBound: Double,
    showSolvingWay: Boolean = false
) {
    private val a: Double
    private val b: Double
    private val show: Boolean

    init {
        this.a = minOf(lowerBound, upperBound)
        this.b = maxOf(lowerBound, upperBound)
        this.show = showSolvingWay
    }

    private fun pointsLog(points: Map<Double, Double>,x:String="x",y:String="y") {
        if (!show) return
        val width = 8

        points.entries.forEachIndexed { i, entry ->
            val formattedX = "%.6f".format(entry.key).trimEnd('0').padEnd(width, ' ')
            val formattedY = "%.6f".format(entry.value).trimEnd('0').padStart(width, ' ')
            println("$x$i: $formattedX $y$i: $formattedY")
        }
        println("result = sum = ${points.values.sum()} ")

    }

    fun leftRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        val points = (0 until n)
            .map {i-> a + i * h }
            .associateWith { x -> function(x) }
        pointsLog(points)
        return points.values.sum() * h
    }

    fun rightRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        val points = (1..n)
            .map {i-> a + i * h }
            .associateWith { x -> function(x) }
        pointsLog(points)
        return points.values.sum() * h
    }

    fun middleRectangleMethod(n: Int): Double {
        val h = (b - a) / n
        val points =  (0 until n)
            .map { a + it * h }
            .map { (it + (it + h)) / 2 }
            .associateWith { x -> function(x) }
        pointsLog(points,"(xi + xi+1)/2",)
        return points.values.sum() * h
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
        val xValues = (0 until n).map { a + it * h }
        val first = function(a) + function(b)
        val second = 4 * xValues.filterIndexed { index, _ -> index % 2 == 1 }.sumOf { function(it) }
        val third = 2 * xValues.filterIndexed { index, _ -> index % 2 == 0 && index != 0 && index != n }.sumOf { function(it) }
        return (first + second + third) * h/3.0
    }
    fun interpolationMethod(n: Int): Double{
        val h = (b - a) / (n-1).toDouble()
        val points = (0 until n)
            .map {i-> a + i * h }
            .associateWith { x -> function(x) }
        pointsLog(points)
        val poly = NewtonPolynomial(points)
        val integral = poly.antiderivative()
        if(show){
            println("Создаем Полином Лагранжа и берем от него интеграл Римана ")
            println("L= $poly")
            println("F(L)= $integral")
            println("I= F(L) = F(L)(b) - F(F)(a) = ${integral(b)-integral(a)}")
        }
        return poly.rhimanIntegral(a,b)
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
}