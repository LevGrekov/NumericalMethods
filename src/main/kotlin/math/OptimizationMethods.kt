package math

import kotlin.math.abs

object OptimizationMethods {
    fun findRoots(a: Double, b: Double, function: (Double) -> Double, derivative: (Double) -> Double, epsilon: Double = 10e-6): List<Double> {
        val (start, end) = if (a <= b) a to b else b to a
        var prev = start
        var curr = prev + epsilon
        val roots: MutableList<Double> = mutableListOf()
        while (curr <= end) {
            if(sgn(function(curr)) != sgn(function(prev))){
                val root = newtonMethod(curr,function,derivative)
                roots.add(root)
            }
            prev = curr
            curr+= epsilon
        }
        return roots
    }

    private fun newtonMethod(initialGuess: Double, function: (Double) -> Double, derivative: (Double) -> Double, tol: Double = 1e-7, maxIter: Int = 1000): Double {
        var x = initialGuess
        var iteration = 0

        while (abs(function(x)) > tol && iteration < maxIter) {
            x -= function(x) / derivative(x)
            iteration++
        }

        return x
    }
}