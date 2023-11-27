import TestsSolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import math.splines.CubeSpline
import math.splines.SplineCalculationMethod
import kotlin.math.*

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "Вторая Контрольная По Численным Методам",
//        state = WindowState(width = 800.dp, height = 500.dp)
//    ) {
////        SecondTestSolver.solve(
////            {x -> exp(x/2) + cosh((x+1)/2)},
////            lowLim = -1.0,
////            upLim = 1.0,
////        )
////        SecondTestSolver.solve(
////            {x -> exp(x/2) + 2.0.pow(((2.0*x+1.0)/3.0))},
////            lowLim = -1.0,
////            upLim = 1.0,
////            -5.0/8.0, 5.0/7.0, 2.0/7.0
////        )
//
//            SecondTestSolver.solve(
//            {x -> exp(sqrt(x/3.0)*cos(4.0 * x))},
//            0.2,
//                (2.0 * Math.PI)/3.0,
//            0.095*Math.PI, 0.18*Math.PI, 0.42*Math.PI
//        )
//    }
//}

fun main(){

    fun getPoints(
        funcX: (Double, Double, Int) -> Double,
        funcY: (Double) -> Double,
        lowLim: Double,
        upLim: Double,
        k1: Int,
        k2: Int
    ): Map<Double, Double> {
        println("Значение функции в узловых точках:")

        val xyMap = mutableMapOf<Double, Double>()
        for (k in k1..k2) {
            val x = funcX(lowLim, upLim, k)
            val y = funcY(x)
            println("f($x) = $y ")
            xyMap[x] = y
        }
        println("\n")
        return xyMap
    }


    val fy:(Double)->Double = {x -> exp(x/2) + cosh((x+1)/2)}
    val fx: (Double, Double, Int) -> Double = { a, b, k -> a + k * (b - a) / 4.0 }
    val firstPoints = mapOf(-4.0 to 12.0,-2.0 to 2.0,0.0 to 4.0,2.0 to 6.0)

    val moments = CubeSpline(firstPoints,SplineCalculationMethod.MOMENTS)
    val deffinition = CubeSpline(firstPoints,SplineCalculationMethod.DEFINITION)


    println("moments\n")
    println(moments)

    println("deffinition\n")
    println(deffinition)
}