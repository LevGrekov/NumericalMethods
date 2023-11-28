import TestsSolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import math.splines.CubeSpline
import math.splines.SplineCalculationMethod
import kotlin.math.*

/*
* Чтобы использовать приложение, введите в поля ниже ваше значение функции, a,b и контрольные точки.
* В консоль выведится всё что нужно. Если вы скачали этот репозиторий и вам интересно промежуточное решение, напишите мне в ЛС,
* я сделаю чтобы они тоже записывались, а не только конечные данные
* */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Вторая Контрольная По Численным Методам",
        state = WindowState(width = 800.dp, height = 500.dp)
    ) {
            SecondTestSolver.solve(
                {x->exp(x/2.0)+cosh((x+1.0)/2.0)},
                  -1.0,
                  1.0,
                -5.0 / 8.0, 5.0 / 7.0, 2.0 / 7.0
            )
    }
}
