package TestsSolvers

import math.NumericIntegral

object ThirdTestSolver {
    fun solve(firstIntegral:NumericIntegral, secondIntegral: NumericIntegral){

        println("\nПЕРВЫЙ ИНТЕГРАЛ\n")

        firstIntegral.apply {
            println("Левые прямоугольники:")
            val a = leftRectangleMethod(10).also { println("$it\n") }

            println("Правые Прямоугольники:")
            val b = rightRectangleMethod(10).also { println("$it\n") }

            println("Средние прямоугольники:")
            val c = middleRectangleMethod(10).also { println("$it\n") }

            println("Симпсон:")
            val d = simpsonMethod(10).also { println("$it\n") }

            println("Интерполяционный метод:")
            val e = interpolationMethod(10).also { println("$it\n") }

            println("Гаусс rho = 1 (4):")
            val f = gaussianMethodWithRo1(6).also { println("$it\n") }

            println("Гаусс rh0 = 1/sqrt(1-x^2) (6):")
            val h = gaussianMethodWithWithSpecialRo(10).also { println("$it\n") }

        }

        println("\nВТОРОЙ ИНТЕГРАЛ\n")

        secondIntegral.apply {
            println("Левые прямоугольники:")
            val a = leftRectangleMethod(10).also { println("$it\n") }

            println("Правые Прямоугольники:")
            val b = rightRectangleMethod(10).also { println("$it\n") }

            println("Средние прямоугольники:")
            val c = middleRectangleMethod(10).also { println("$it\n") }

            println("Симпсон:")
            val d = simpsonMethod(10).also { println("$it\n") }

            println("Интерполяционный метод:")
            val e = interpolationMethod(10).also{ println("$it\n") }

            println("Гаусс rho = 1 (4):")
            val f = gaussianMethodWithRo1(6).also { println("$it\n") }

            println("Гаусс rh0 = 1/sqrt(1-x^2) (6):")
            val h = gaussianMethodWithWithSpecialRo(10).also { println("$it\n") }

        }

    }
}