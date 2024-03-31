package testsolvers

import math.calculateDerivativeAtPoint
import math.polynomials.LagrangePolynomial
import math.polynomials.NewtonPolynomial
import math.polynomials.Polynomial
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

object FirstTestSolver {

    /*
    Этот класс был сюда всутнут насильно, функционал может быть утерян, а именно промежуточные вычисления полинома лагранжа
     */
    fun solve(a: Double, b: Double, ownFunction: (Double) -> Double, x1 : Double, x2:Double, x3:Double){

        val controlPoints = mapOf(1 to x1, 2 to x2, 3 to x3)

        val firstXY = getPoints(ownFunction,{ k -> a + k * (b-a)/4 },0,4)

        val firstLagrangePolynomial = LagrangePolynomial(firstXY)

        println("Вычислим L5(f, x) в контрольных точках xi, i = 1..3 и сравним со значениями f(xi)\n")
        for (i in controlPoints.keys){
            val xInControlPoint = firstLagrangePolynomial(controlPoints[i]!!).also {
                println("Подставим x$i в полином: L(f,x$i) = $it")
            }
            val xInFunc = ownFunction(controlPoints[i]!!)
            val epsilon = xInFunc - xInControlPoint
            println("r$i = f(x$i) - L(f,x$i) = $xInFunc - $xInControlPoint = $epsilon")
        }

        //////////////////////////////////////////

        println("\n\nПункт 2")
        val secondXY = getPoints(ownFunction,{k -> ((b-a)/2.0) * cos((PI * (2.0*k-1.0))/10.0) + (b+a)/2.0},1,5)
        val secondLagrangePolynomial = LagrangePolynomial(secondXY)
        println("Вычислим L5(f, x) в контрольных точках xi, i = 1..3 и сравним со значениями f(xi)\n")

        for (i in controlPoints.keys){
            val xInControlPoint = secondLagrangePolynomial(controlPoints[i]!!).also {
                println("Подставим x$i в полином: L(f,x$i) = $it")
            }
            val xInFunc = ownFunction(controlPoints[i]!!)
            val epsilon = xInFunc - xInControlPoint
            println("r$i = f(x$i) - L(f,x$i) = $xInFunc - $xInControlPoint = $epsilon")
        }
        val toFindMax = secondLagrangePolynomial.findW()
        println("$toFindMax")

        /////////////////////////////////////////////////////////

        println("\nНаёдм Полином Ньютона.")
        println("Найдем базисные полиномы и разделенные разности для каждого слогаемого")
        println("Базисные Полиномы будем искать используя предыдущие значения")
        val pointsN = getPoints(ownFunction,{ k -> a + k * (b-a)/4 },0,4)
        val np = NewtonPolynomial(pointsN)
        println("Конечный Полином Ньютона будет иметь вид: $np")

        /////////////////////////////////////////////////////////////////

        println("\n\n 3 Задание")
        val points = getPoints(ownFunction,{ k -> a + k * (b-a)/4.0 },0,4)
        val lagrange = LagrangePolynomial(points)
        val w = lagrange.findW()

        val h4List = mutableListOf<Double>()
        points.keys.forEach{
            val funcD =
                calculateDerivativeAtPoint(ownFunction,it,10e-5)
            val l5d = lagrange.derivative(1)(it)
            val wd = w.derivative(1)(it)
            h4List.add( (funcD - l5d)/wd )
        }

        val system = generateMatrix(points)
        val solution = SystemSolver.gaussMethod(system,h4List.toDoubleArray())?.let{
            val H4 = Polynomial(*it)
            val H9 = lagrange + w * H4
            println("H4 = $H4")
            println("H9 = $H9")
            println(H9)
            println("Вычисляем погрешности")
            for (i in controlPoints.keys){
                val xInControlPoint = H9(controlPoints[i]!!)
                val xInFunc = ownFunction(controlPoints[i]!!)
                val epsilon = xInFunc - xInControlPoint
                println("r_$i = f'(x^($i)) - H'_9(f,x) = $xInFunc - $xInControlPoint = $epsilon")
            }
            for (i in controlPoints.keys){
                val xInControlPoint = H9.derivative(1)(controlPoints[i]!!)
                val xInFunc = calculateDerivativeAtPoint(ownFunction,controlPoints[i]!!)
                val epsilon = xInFunc - xInControlPoint
                println("r^d_$i = f(x^($i)) - H_9(f,x) = $xInFunc - $xInControlPoint = $epsilon")
            }
        }
    }

    fun firstExercise2(){

    }
    fun secondExercise(){


    }

    fun thirdExercise(){

    }

    private fun generateMatrix(points: Map<Double,Double>) : Array<DoubleArray> {
        val system: Array<DoubleArray> = Array(5) { DoubleArray(5) }
        var i = 0
        points.keys.forEach{
            val p = doubleArrayOf(1.0, it.pow(1.0), it.pow(2.0), it.pow(3.0), it.pow(4.0))
            system[i] = p
            i++
        }
        return system
    }
    private fun getPoints(ownFunction:  (Double) -> Double ,func: (Double) -> Double, lowLim: Int, upLim: Int): Map<Double, Double> {
        val xyMap = mutableMapOf<Double, Double>()
        println("Найдем x,y для построения полинома")
        for (k in lowLim..upLim) {
            val x = func(k.toDouble())
            val y = ownFunction(x)
            println("На шаге $k x = $x")
            println("y = $y")
            xyMap[x] = y
        }
        println("\n")
        return xyMap
    }
}