package testsolvers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import drawing.compose.showPlot
import drawing.painters.GPainter
import drawing.painters.PointsPainter
import drawing.painters.functions.UsualFunctionPainter
import math.findMaxError
import math.polynomials.NewtonPolynomial
import math.splines.CubeSpline
import math.splines.LinearSpline
import math.splines.SplineCalculationMethod
import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random

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

    private fun checkError(
        name: String,
        controlPoints: DoubleArray,
        originalF: (Double) -> Double?,
        yourFunk: (Double) -> Double?
    ) {
        println("Вычислим $name в контрольных точках xi, i = 1..3 и сравним со значениями f(xi)")
        controlPoints.forEachIndexed { i, x ->
            val xInControlPoint = yourFunk(x)
            val xInFunk = originalF(x)
            val result = xInControlPoint?.let { controlPoint ->
                xInFunk?.let { funk ->
                    val eps = funk - controlPoint
                    "r$i = f(x$i) - S(x$i) = ${"%.6f".format(funk)} - ${"%.6f".format(controlPoint)} = ${"%.6f".format(eps)}"
                } ?: "Оригинальная Функция не существует в точке $x"
            } ?: "Ваша Функция не существует в точке $x"
            println(result)
        }
    }

    private fun generateDoubleMap(size: Int): Map<Double, Double> {
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

        val functionTuples: MutableList<GPainter> = mutableListOf()


        functionTuples.add(GPainter(UsualFunctionPainter(fy,Color(66, 170, 255,)),null,"f(x)"))

        val errors: MutableMap<String, Double> = mutableMapOf()

        val pointRadius = 2f

        val ls1 = LinearSpline(firstPoints  ).also {
            val name = "S₁(x)"
            print("Первый Линенйный Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(229,43,80)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(firstPoints,color,pointRadius),name))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val ls2 = LinearSpline(secondPoints).also {
            val name = "S₂(x)"
            print("Второй Линенйный Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(68,148,74)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(secondPoints,color,pointRadius),name))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val cs1 = CubeSpline(firstPoints,method = SplineCalculationMethod.MOMENTS).also {
            val name = "S₁³(x)"
            val cs1d = CubeSpline(firstPoints,method = SplineCalculationMethod.DEFINITION)
            print("Первый Кубический Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(255,176,46)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(firstPoints,color,pointRadius),name))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val cs2 = CubeSpline(secondPoints, method = SplineCalculationMethod.MOMENTS).also {
            val name = "S₂³(x)"
            val cs2d = CubeSpline(secondPoints,method = SplineCalculationMethod.DEFINITION)

            print("Второй Кубический Сплайн\n$it")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(189,	51,	164)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(secondPoints,color,pointRadius),name))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        println("##########################################################3 Дополнительная часть 3##########################################################")

        val L1 = NewtonPolynomial(firstPoints).also {
            val name = "L₁(x)"
            print("Первый Полином Лагранжа\n$it\n")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(255,79,0)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(firstPoints,color,pointRadius),name))
            val error = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        val L2 = NewtonPolynomial(secondPoints).also {
            val name = "L₂(x)"
            print("Второй Полином Лагранжа\n$it\n")
            checkError(name,controlPoints,fy,it::invoke)
            val color = Color(0	,71,171)
            functionTuples.add(GPainter(UsualFunctionPainter(it::invoke,color),PointsPainter(secondPoints,color,pointRadius),name))
            val error = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),fy,it::invoke)
            print("Максимальная Погрешность на на промежутке = $error\n\n")
            errors[name]=error
        }

        errors.entries.minBy { it.value }.run { println("Минимальная максимальная погрешность = $value у Интерполянта $key\n") }

        val errorLS1 = findMaxError(firstPoints.keys.first(),firstPoints.keys.last(),cs1::invoke,L1::invoke)
        print("Максимальная Погрешность S₁³(x) и L₁(x) = $errorLS1\n\n")

        val errorLS2 = findMaxError(secondPoints.keys.first(),secondPoints.keys.last(),cs2::invoke,L2::invoke)
        print("Максимальная Погрешность S₂³(x) и L₂(x) = $errorLS2\n\n")


        val pointsIntest = 300
        val doubleMapArray = generateDoubleMap(pointsIntest)

//        println("Проверим скорость рачета Кубического сплайна")
//        println("Создадим $pointsIntest точек и сравним время вычисления по определению и через моменты")
//
//        val defTime = measureNanoTime {
//            val a = CubeSpline(doubleMapArray, method = SplineCalculationMethod.DEFINITION)
//        }
//
//        val momentsTime = measureNanoTime {
//            val b = CubeSpline(doubleMapArray)
//        }
//
//        val effCoff = defTime.toDouble()/momentsTime.toDouble()
//        println("(по Определению) / (метод моментов) = $defTime / $momentsTime = $effCoff")
//        println("Таким образом метод моментов быстрее, чем по определению в $effCoff раз для $pointsIntest Узлов  ")

        var xMin = (firstPoints + secondPoints).keys.min()
        var xMax = (firstPoints + secondPoints).keys.max()
        var yMin = (firstPoints + secondPoints).values.min()
        var yMax = (firstPoints + secondPoints).values.max()


        //showPlot(functionTuples,xMin,xMax,yMin,yMax)
        showPlot(functionTuples)
    }
}