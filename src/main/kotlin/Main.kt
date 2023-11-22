import TestsSolvers.SecondTestSolver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.math.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PolynomialBuilder",
        state = WindowState(width = 600.dp, height = 500.dp)
//        resizable = false,
    ) {
        val fx : (Double) -> Double = {x -> exp(x/2) + cosh((x+1)/2)}
        SecondTestSolver.solve(
            {x -> exp(x/2) + cosh((x+1)/2)},
            lowLim = -1.0,
            upLim = 1.0,
        )
//        SecondTestSolver.solve(
//            {x -> exp(-x.pow(3))},
//            -2.0,
//            -1.0,
//            -1.29, -1.42, -1.76
//        )
//            SecondTestSolver.solve(
//            {x -> exp(sqrt(x/3.0)*cos(4.0 * x))},
//            0.2,
//                (2.0 * Math.PI)/3.0,
//            0.095*Math.PI, 0.18*Math.PI, 0.42*Math.PI
//        )
    }
}

//import kotlin.math.pow
//
//class CubicSplineInterpolator(private val x: DoubleArray, private val y: DoubleArray) {
//
//    private val n: Int = x.size - 1
//    private val a: DoubleArray
//    private val b: DoubleArray
//    private val c: DoubleArray
//    private val d: DoubleArray
//
//    init {
//        require(x.size == y.size) { "Количество точек должно быть одинаковым" }
//
//        // Решение системы линейных уравнений для нахождения коэффициентов сплайна
//        val h = DoubleArray(n)
//        val alpha = DoubleArray(n)
//        val l = DoubleArray(n + 1)
//        val mu = DoubleArray(n + 1)
//        val z = DoubleArray(n + 1)
//
//        for (i in 0 until n) {
//            h[i] = x[i + 1] - x[i]
//            alpha[i] = 3 * (y[i + 1] - y[i]) / h[i] - 3 * (y[i] - if (i > 0) y[i - 1] else 0.0) / (if (i > 0) h[i - 1] else 1.0)
//        }
//
//        l[0] = 1.0
//        mu[0] = 0.0
//        z[0] = 0.0
//
//        for (i in 1 until n) {
//            l[i] = 2.0 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1]
//            mu[i] = h[i] / l[i]
//            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i]
//        }
//
//        l[n] = 1.0
//        z[n] = 0.0
//        c = DoubleArray(n + 1)
//        b = DoubleArray(n)
//        d = DoubleArray(n)
//
//        c[n] = 0.0
//
//        for (j in n - 1 downTo 0) {
//            c[j] = z[j] - mu[j] * c[j + 1]
//            b[j] = (y[j + 1] - y[j]) / h[j] - h[j] * (c[j + 1] + 2 * c[j]) / 3
//            d[j] = (c[j + 1] - c[j]) / (3 * h[j])
//        }
//
//        a = y.copyOf(n)
//        println("\n")
//        val mtrx = arrayOf(a, b, c, d)
//        printMatrix(mtrx)
//        println("\n")
//    }
//
//    fun interpolate(xi: Double): Double {
//        // Находим интервал, в который попадает xi
//        var index = 0
//        while (index < n && xi > x[index + 1]) {
//            index++
//        }
//
//        // Вычисляем значение сплайна в точке xi
//        val dx = xi - x[index]
//        return a[index] + b[index] * dx + c[index] * dx.pow(2) + d[index] * dx.pow(3)
//    }
//}
//
//fun main() {
//    // Пример данных
//    val x = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0)
//    val y = doubleArrayOf(21.0, 24.0, 24.0, 18.0, 16.0)
//
//    // Создаем интерполятор
//    val interp = CubicSplineInterpolator(x, y)
//
//    // Интерполируем значение в точке
//    val xi = 1.5
//    val yi = interp.interpolate(xi)
//
//    // Вывод результата
//    println("Интерполированное значение в точке $xi: $yi")
//}
//
//fun printMatrix(matrix: Array<DoubleArray>) {
//    for (row in matrix) {
//        for (element in row) {
//            print("$element ")
//        }
//        println()
//    }
//}

