package TestsSolvers

import math.NumericIntegral

object ThirdTestSolver {
    fun solve(firstIntegral:NumericIntegral, secondIntegral: NumericIntegral){
        val answerSquares = firstIntegral.calculateRectangleMethod(10)
        println(answerSquares)

        val anwserMidSquares = firstIntegral.calculateTrapezoidalMethod(10)
        println(anwserMidSquares)

        val answerSimpson = firstIntegral.calculateSimpsonMethod(10)
        println(answerSimpson)

        val answerGauss = firstIntegral.gaussianMethodWithRo1(6)
        println(answerGauss)

        val answerGauss2 = firstIntegral.gaussianMethodWithWithSpecialRo(6)
        println("Салам")
        println(answerGauss2)

        val interpolAnswer = firstIntegral.InterpolationMethod(10)
        println("Интерпол")
        println(interpolAnswer)
    }
}