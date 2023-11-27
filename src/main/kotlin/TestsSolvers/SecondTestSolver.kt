package TestsSolvers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import drawing.compose.showPlot
import math.findMaxError
import math.polynomials.NewtonPolynomial
import math.splines.CubeSpline
import math.splines.LinearSpline
import math.splines.SplineCalculationMethod
import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

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

    private fun checkError(name:String,controlPoints: DoubleArray, originalF :(Double)->Double?, yourFunk :(Double)->Double?){
        println("Вычислим $name в контрольных точках xi, i = 1..3 и сравним со значениями f(xi)")
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
    }

    fun generateDoubleMap(size: Int): Map<Double, Double> {
        val map = mutableMapOf<Double, Double>()
        repeat(size) {
            val key = Random.nextDouble(0.0, 100.0)
            val value = Random.nextDouble(0.0, 100.0)
            map[key] = value
        }
        return map
    }

    @Composable
    fun solve(fy: (Double) -> Double, lowLim: Double, upLim: Double, vararg controlPoints: Double) {

        val fx: (Double, Double, Int) -> Double = { a, b, k -> a + k * (b - a) / 4.0 }
        val firstPoints = getPoints(fx, fy, lowLim, upLim, 0, 4)

        val fx2: (Double, Double, Int) -> Double = {a,b,k -> (b-a)/2 * cos(((2*k - 1)* PI)/10) + (b+a)/2}
        val secondPoints = getPoints(fx2, fy, lowLim, upLim, 1, 5)

        val functionTuples: MutableList<Triple<(Double) -> Double?, Map<Double, Double>?,Triple<Color,String,Boolean>>> = mutableListOf()

        functionTuples.add(Triple(fy,null,Triple(Color(66, 170, 255,),"f(x)",true)))

        val errors: MutableMap<String, Double> = mutableMapOf()

        val ls1 = LinearSpline(firstPoints).also {
            val name = "S₁(x)"
            print("Первый Линенйный Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, firstPoints,Triple(Color(229,43,80), name ,true)))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val ls2 = LinearSpline(secondPoints).also {
            val name = "S₂(x)"
            print("Второй Линенйный Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, secondPoints, Triple(Color(68,148,74),name,true)))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val cs1 = CubeSpline(firstPoints).also {
            val name = "S₁³(x)"
            print("Первый Кубический Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, firstPoints,Triple(Color(255,176,46),name ,true)))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val cs2 = CubeSpline(secondPoints).also {
            val name = "S₂³(x)"
            print("Второй Кубический Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, secondPoints, Triple(Color(189,	51,	164),name,true)))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        println("##########################################################3 Дополнительная часть 3##########################################################")

        val L1 = NewtonPolynomial(firstPoints).also {
            val name = "L₁(x)"
            print("Первый Полином Лагранжа\n$it\n")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, firstPoints, Triple(Color(255,79,	0),name,false)))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val L2 = NewtonPolynomial(secondPoints).also {
            val name = "L₂(x)"
            print("Второй Полином Лагранжа\n$it\n")
            checkError(name,controlPoints,fy,it::invoke)
            functionTuples.add(Triple(it::invoke, secondPoints,Triple(Color(0	,71	,171),name,false)))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        errors.entries.minBy { it.value }.run { println("Минимальная максимальная погрешность = $value у Интерполянта $key\n") }

        val errorLS1 = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),cs1::invoke,L1::invoke)
        print("Максимальная Погрешность S₁³(x) и L₁(x) = $errorLS1\n\n")

        val errorLS2 = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),cs2::invoke,L2::invoke)
        print("Максимальная Погрешность S₂³(x) и L₂(x) = $errorLS2\n\n")

        println("Проверим скорость рачета Кубического сплайна")
        println("Создадим 200 точек и посмотрим на их примере в наносекундах")
        val doubleMapArray = generateDoubleMap(200)

        val defTime = measureNanoTime {
            val a = CubeSpline(doubleMapArray)
        }

        val momentsTime = measureNanoTime {
            val b = CubeSpline(doubleMapArray, method = SplineCalculationMethod.MOMENTS)
        }

        val effCoff = defTime.toDouble()/momentsTime.toDouble()
        println("(по Определению) / (метод моментов) = $defTime / $momentsTime = $effCoff")
        println("Таким образом метод моментов быстрее, чем по определению в $effCoff раз ")

        var xMin = (firstPoints + secondPoints).keys.min()
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