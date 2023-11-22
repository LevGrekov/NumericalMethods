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
        SecondTestSolver.solve(
            {x -> exp(-x.pow(3))},
            -2.0,
            -1.0,
            -1.29, -1.42, -1.76
        )
    }
}

