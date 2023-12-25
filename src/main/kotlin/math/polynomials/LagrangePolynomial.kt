package math.polynomials

import math.neq

class LagrangePolynomial(points: Map<Double,Double>) : Polynomial() {
    private val _points: MutableMap<Double, Double>
    init {
        _points = points.toMutableMap()
        if(_points.isEmpty()) _coeffs[0] = 0.0
        else _coeffs.apply {
            clear()
            putAll(createLagrangePoly().coeffs)
        }
    }
    val points: Map<Double,Double>
        get()= _points.toMap()

    private fun createLagrangePoly(): Polynomial = _points.entries.fold(Polynomial(mapOf(0 to 0.0))) {result, (x,fx) -> result + createFundamentalPoly(_points.keys.toList(),x) * fx}


    companion object{
        fun createFundamentalPoly(points:List<Double>,xk: Double): Polynomial =
            points.fold(Polynomial(1.0)) { acc, it -> if (xk neq it) acc * Polynomial(-it, 1.0) / (xk - it) else acc }
    }
}