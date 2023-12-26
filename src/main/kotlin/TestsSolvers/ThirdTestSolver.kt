package TestsSolvers

import math.NumericIntegral

object ThirdTestSolver {
    fun solve(firstIntegral:NumericIntegral, secondIntegral: NumericIntegral){

        firstIntegral.apply {
            println("Левые прямоугольники: ${leftRectangleMethod(10)}")
            println("Правые прямоугольники: ${rightRectangleMethod(10)}")
            println("Средние прямоугольники: ${middleRectangleMethod(10)}")
            println("Симпсона: ${simpsonMethod(10)}")
            println("Гаусс rho = 1 (4): ${gaussianMethodWithRo1(6)}")
            println("Интерполяционный метод: ${interpolationMethod(10)}")
            println("Гаусс rh0 = 1/sqrt(1-x^2) (6): ${gaussianMethodWithRo1(6)}")
        }

        println("\n")

        secondIntegral.apply {
            println("Левые прямоугольники: ${leftRectangleMethod(10)}")
            println("Правые прямоугольники: ${rightRectangleMethod(10)}")
            println("Средние прямоугольники: ${middleRectangleMethod(10)}")
            println("Симпсона: ${simpsonMethod(10)}")
            println("Гаусс rho = 1 (4): ${gaussianMethodWithRo1(6)}")
            println("Интерполяционный метод: ${interpolationMethod(10)}")
            println("Гаусс rh0 = 1/sqrt(1-x^2) (6): ${gaussianMethodWithRo1(6)}")
        }

    }
}