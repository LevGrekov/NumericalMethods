package TestsSolvers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import drawing.compose.showPlot
import math.splines.CubeSpline
import math.splines.LinearSpline
import kotlin.math.PI
import kotlin.math.cos

object SecondTestSolver {
    private fun getPoints(
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

    private fun checkError(controlPoints: DoubleArray, originalF :(Double)->Double?, yourFunk :(Double)->Double?){
        println("Вычислим Сплайн в контрольных точках xi, i = 1..3 и сравним со значениями f(xi)")
        controlPoints.forEachIndexed { i, x ->
            val xInControlPoint = yourFunk(x)
            val xInFunk = originalF(x)
            val result = xInControlPoint?.let { controlPoint ->
                xInFunk?.let { funk ->
                    val eps = funk - controlPoint
                    "r$i = f(x$i) - S(x$i) = $funk - $controlPoint = $eps"
                } ?: "Оригинальная Функция не существует в точке $x"
            } ?: "Ваша Функция не существует в точке $x"
            println(result)
        }
        println("\n")
    }

    @Composable
    fun solve(fy: (Double) -> Double, lowLim: Double, upLim: Double, vararg controlPoints: Double) {

        val fx: (Double, Double, Int) -> Double = { a, b, k -> a + k * (b - a) / 4.0 }
        val firstPoints = getPoints(fx, fy, lowLim, upLim, 0, 4)

        val fx2: (Double, Double, Int) -> Double = {a,b,k -> (b-a)/2 * cos(((2*k - 1)* PI)/10) + (b+a)/2}
        val secondPoints = getPoints(fx2, fy, lowLim, upLim, 1, 5)
        val functionTuples: MutableList<Triple<(Double) -> Double?, Map<Double, Double>?,Pair<Color,String>>> = mutableListOf()

        functionTuples.add(Triple(fy,null,Color(66, 170, 255) to "f(x)"))

        val ls1 = LinearSpline(firstPoints).also {
            print("Первый Линенйный Сплайн\n$it")
            checkError(controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, firstPoints,Color(229,43,80) to "S₁(x)"))
        }

        val ls2 = LinearSpline(secondPoints).also {
            print("Второй Линенйный Сплайн\n$it")
            checkError(controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, secondPoints,Color(68,148,74) to "S₂(x)"))
        }

        val cs1 = CubeSpline(firstPoints).also {
            print("Первый Кубический Сплайн\n$it")
            checkError(controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, firstPoints,Color(255,176,46) to "S₁³(x)"))
        }

        val cs2 = CubeSpline(secondPoints).also {
            print("Второй Кубический Сплайн\n$it")
            checkError(controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, secondPoints,Color(189,	51,	164) to "S₂³(x)"))
        }

        var xMin = (firstPoints + secondPoints).keys.min()
        var xMax = (firstPoints + secondPoints).keys.max()
        var yMin = (firstPoints + secondPoints).values.min()
        var yMax = (firstPoints + secondPoints).values.max()
        val delta = (xMax + xMin + yMax + yMin)/8.0
        val deltaX = (xMax-xMin)
        xMax += deltaX/10.0
        xMin -= deltaX/10.0
        yMin -= delta
        yMax += delta

        showPlot(functionTuples,xMin,xMax,yMin,yMax)
    }
}