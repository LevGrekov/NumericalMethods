import TestsSolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import math.splines.CubeSpline
import math.splines.SplineCalculationMethod
import kotlin.math.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Вторая Контрольная По Численным Методам",
        state = WindowState(width = 800.dp, height = 500.dp)
    ) {
//        SecondTestSolver.solve(
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
        SecondTestSolver.solve(
            {x-> ln(x*x) + (x*x*x)},
            2.0,
            4.0,
            2.89, 2.42, 2.26
        )
    }
}
