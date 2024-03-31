import testsolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import math.complex.ComplexMatrix
import math.complex.ComplexNum
import testsolvers.FourthTestSolver
import testsolvers.SquareRootMethodSLAU
import kotlin.math.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Третья Контрольная По Численным Методам",
        state = WindowState(width = 800.dp, height = 500.dp)
    ) {
//        val firstIntegral = NumericIntegral({ x-> sqrt(2.0 * x * x + 1.6)/(2.0 * x + sqrt(0.5 * x * x + 3.0))},1.2,2.0,true)
//        val secondIntegral = NumericIntegral({ x-> sin(0.5*x+0.4)/(1.2+cos(x*x+0.4))},0.5,1.3,true)
//        ThirdTestSolver.solve(firstIntegral,secondIntegral)
//        SecondTestSolver.solve(
//            {x->exp(x/2.0)+cosh((x+1.0)/2.0)},
//            -1.0,
//            1.0,
//            -5.0 / 8.0, 5.0 / 7.0, 2.0 / 7.0
//        )

        FourthTestSolver.firstEx(
            ComplexMatrix(3,3,
                arrayOf(
                    arrayOf(ComplexNum(1.48),ComplexNum(0.75),ComplexNum(-1.23)),
                    arrayOf(ComplexNum(0.75),ComplexNum(0.96),ComplexNum(1.64)),
                    arrayOf(ComplexNum(-1.23),ComplexNum(1.64),ComplexNum(-0.55))
                )
            ),
            ComplexMatrix(1,3,arrayOf(arrayOf(ComplexNum(0.83),ComplexNum(-1.12),ComplexNum(0.47))))
        )
//        val a = ComplexNum(1.12, 12.1)
//        val b = ComplexNum(1.54, 0.0)
//
//        println(a*b)
//        println(a-b)
//        println(a/b)
//        println(a+b)

//        println("#############################################################################")
//
//
//        val a =ComplexMatrix(3,3,
//            arrayOf(
//                arrayOf(ComplexNum(1.48),ComplexNum(0.75),ComplexNum(-1.23)),
//                arrayOf(ComplexNum(0.75),ComplexNum(0.96),ComplexNum(1.64)),
//                arrayOf(ComplexNum(-1.23),ComplexNum(1.64),ComplexNum(-0.55))
//            )
//        )
//        println(a[0,2])
//
//        val b = ComplexMatrix(3,3,
//            arrayOf(
//                arrayOf(ComplexNum(1.48),ComplexNum(0.75),ComplexNum(-1.23)),
//                arrayOf(ComplexNum(0.75),ComplexNum(0.96),ComplexNum(1.64)),
//                arrayOf(ComplexNum(-1.23),ComplexNum(1.64),ComplexNum(-0.55))
//            )
//        ).invertibleMatrix()
//
//        println(a*b)
    }

}



fun old(){
//            SecondTestSolver.solve(
//            {x->exp(x/2.0)+cosh((x+1.0)/2.0)},
//            -1.0,
//            1.0,
//            -5.0 / 8.0, 5.0 / 7.0, 2.0 / 7.0
//        )

//        SecondTestSolver.solve(
//            {x->exp(sqrt(x/3) * cos(4*x))},
//            0.2,
//            (2.0* PI)/3.0,
//            0.095*PI, 0.18*PI, 0.42* PI
//        )
//        SecondTestSolver.solve(
//            {x-> ln(x*x) + (x*x*x)},
//            2.0,
//            4.0,
//            2.89, 2.42, 2.26
//       )

//        val a = IntegralAproximator.calculateRectangleMethod({ x->exp(x.pow(2.0))},0.0,1.0,1000000).also { println(it) }
//        val b = IntegralAproximator.calculateTrapezoidalMethod({ x->exp(x.pow(2.0))},0.0,1.0,117).also { println(it) }
//        val c = IntegralAproximator.calculateTrapezoidalMethodWithMinN({ x->exp(x.pow(2.0))},0.0,1.0,10e-4).also { println(it) }
}