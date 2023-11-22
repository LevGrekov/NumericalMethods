import TestsSolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.math.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Вторая Контрольная По Численным Методам",
        state = WindowState(width = 800.dp, height = 500.dp)
    ) {
//        SecondTestSolver.solve(
//            {x -> exp(x/2) + cosh((x+1)/2)},
//            lowLim = -1.0,
//            upLim = 1.0,
//        )
//        SecondTestSolver.solve(
//            {x -> exp(x/2) + 2.0.pow(((2.0*x+1.0)/3.0))},
//            lowLim = -1.0,
//            upLim = 1.0,
//            -5.0/8.0, 5.0/7.0, 2.0/7.0
//        )

            SecondTestSolver.solve(
            {x -> exp(sqrt(x/3.0)*cos(4.0 * x))},
            0.2,
                (2.0 * Math.PI)/3.0,
            0.095*Math.PI, 0.18*Math.PI, 0.42*Math.PI
        )
    }
}

