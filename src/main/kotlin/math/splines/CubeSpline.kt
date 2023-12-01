package math.splines

import SystemSolver
import SystemSolver.printMatrix
import math.polynomials.Polynomial
import java.util.*
import kotlin.math.pow

enum class SplineCalculationMethod {
    DEFINITION,
    MOMENTS,
}

class CubeSpline
    (pts: Map<Double, Double>,
     val method:SplineCalculationMethod = SplineCalculationMethod.MOMENTS) : SegmentedFunction() {
    val points: Map<Double, Double> = TreeMap(pts)

    val x = this.points.keys.toList()
    val y = this.points.values.toList()

    fun h(i:Int) = x[i] - x[i-1]

    init {
        segments.apply {
            clear()
            addAll(buildSegments())
        }
    }
    private fun buildSegments(): List<SplineSegment> {
        val coeff = when (method) {
            SplineCalculationMethod.DEFINITION -> getCoefficientsDefinition()
            SplineCalculationMethod.MOMENTS -> getMomentsCoefficients()
        }

        val segments = mutableListOf<SplineSegment>()
        val iterator = points.keys.iterator()

        coeff?.let {

            when(method){
                SplineCalculationMethod.DEFINITION ->{
                    if (iterator.hasNext()) {
                        var x1 = iterator.next()
                        var segInd = 0

                        while (iterator.hasNext()) {
                            val x2 = iterator.next()

                            val polynomial = Polynomial(
                                coeff[4 * segInd+3],
                                coeff[4 * segInd+2],
                                coeff[4 * segInd+1],
                                coeff[4 * segInd])
                            segments.add(SplineSegment(x1, x2, polynomial))

                            x1 = x2
                            segInd++
                        }
                    }
                }
                SplineCalculationMethod.MOMENTS -> {
                    for(i in 1 until points.size){
                        val cube = (coeff[i] - coeff[i-1])/(6.0*h(i))
                        val sq = ((coeff[i-1]*x[i] - coeff[i]*x[i-1])/(2.0*h(i)))
                        val lin = (h(i) * coeff[i-1] - coeff[i]*h(i) + (3.0*coeff[i] * x[i-1].pow(2.0) - 3.0 * coeff[i-1] * x[i].pow(2.0) + 6.0 * y[i] - 6.0 * y[i-1])/h(i))/6.0
                        val const = (coeff[i]*h(i)*x[i-1]-h(i)*coeff[i-1]*x[i] + (-coeff[i]*x[i-1].pow(3.0)+coeff[i-1] * x[i].pow(3.0) - 6.0 * x[i-1] * y[i] + 6.0 * x[i] * y[i-1])/h(i))/6.0
                        val poly = Polynomial(const,lin,sq,cube)
                        segments.add(SplineSegment(x[i-1], x[i], poly))
                    }
                }
            }
        }
        return segments
    }

    private fun getMomentsCoefficients():DoubleArray {

        fun lambda(k:Int) = h(k+1)/(h(k)+h(k+1))
        fun mu(k:Int) = h(k)/(h(k)+h(k+1))
        fun d(k:Int) = (6.0/(h(k)+h(k+1))) * ((y[k+1] - y[k])/h(k+1) - (y[k] - y[k-1])/h(k))

        val matrixSize = points.size

        val matrix = Array(matrixSize) { DoubleArray(matrixSize) }
        val constants = DoubleArray(matrixSize)


        for (i in 1 until matrixSize-1) {
            matrix[i][i-1] = mu(i)
            matrix[i][i] = 2.0
            matrix[i][i+1] = lambda(i)
            constants[i] = d(i)
        }

        matrix[0][0] = 1.0
        constants[0] = 0.0
        matrix[matrixSize - 1][matrixSize - 1] = 1.0
        constants[matrixSize - 1] = 0.0

        if(points.size<20){

            println("\nМетод Моментов. Решение")
            println("СЛУ")
            printMatrix(matrix)
            println("Это свободные члены ")
            printMatrix(constants)
        }

        return SystemSolver.solveTridiagonalMatrixAkaThomasMethod(matrix,constants).also {
            println("Это решение СЛАУ\n")
            printMatrix(it)
        }
    }

    private fun getCoefficientsDefinition(): DoubleArray?{
        val n = points.size - 1
        val x = this.points.keys.toList()
        val y = this.points.values.toList()
        val matrixSize = n * 4
        val matrix = Array(matrixSize) { DoubleArray(matrixSize) }
        val constants = DoubleArray(matrixSize)
        // s = a(x-x0)^3 + b(x-x0)^2 + c(x-x0) + d
        for(i in 0 until n ){
            //ax^3+bx^2+cx+d
            val insertVecMin = doubleArrayOf(x[i].pow(3),x[i].pow(2),x[i],1.0)
            val insertVecMax = doubleArrayOf(x[i+1].pow(3),x[i+1].pow(2),x[i+1],1.0)
            matrix[2*i] = insertVector(matrix[2*i],insertVecMin,i*4)
            constants[2*i] = y[i]
            matrix[2*i+1] = insertVector(matrix[2*i+1],insertVecMax,i*4)
            constants[2*i+1] = y[i+1]
        }
        for(i in 0 until n-1){
            val insertVecFirstD = doubleArrayOf(3.0*x[i+1].pow(2),2*x[i+1],1.0,0.0,-3.0*x[i+1].pow(2),-2*x[i+1],-1.0,0.0)
            val insertVecSecondD = doubleArrayOf(6*x[i+1],2.0,0.0,0.0,-6*x[i+1],-2.0,0.0,0.0)
            matrix[2*n+i] = insertVector(matrix[2*n+i],insertVecFirstD,i*4)
            matrix[3*n-1+i] = insertVector(matrix[3*n-1+i],insertVecSecondD,i*4)
        }

        val insertVecSecondDStart = doubleArrayOf(6*x[0],2.0,0.0,0.0)
        val insertVecSecondDEnd = doubleArrayOf(6*x.last(),2.0,0.0,0.0)
        matrix[n*4-2] = insertVector(matrix[n*4-2],insertVecSecondDStart,0)
        matrix[n*4-1] = insertVector(matrix[n*4-1],insertVecSecondDEnd, matrix[n*4-1].size-4 )
        constants[n*4-1] = 0.0
        constants[n*4-1] = 0.0

        if(points.size<20){
            println("\nПо определению. Решение")
            println("СЛУ")
            printMatrix(matrix)
            println("Это свободные члены ")
            printMatrix(constants)
        }

         return SystemSolver.gaussMethod(matrix,constants).also {
             println("Это решение СЛАУ\n")
             printMatrix(it!!)
         }
    }
    private fun insertVector(originalVector: DoubleArray, insertVector: DoubleArray, position: Int): DoubleArray {
        require(position >= 0 && position + insertVector.size <= originalVector.size) {
            "Позиция вставки выходит за пределы размера исходного вектора."
        }
        val resultVector = originalVector.copyOf()
        System.arraycopy(insertVector, 0, resultVector, position, insertVector.size)
        return resultVector
    }
    private fun computeDerivative(ord: Int): List<SplineSegment> {
        val derivativeSegments = mutableListOf<SplineSegment>()

        for (segment in segments) {
            val derivativePolynomial = segment.polynomial.derivative(ord)

            // Создайте новый сегмент с производной полинома
            val derivativeSegment = SplineSegment(segment.inf, segment.sup, derivativePolynomial)
            derivativeSegments.add(derivativeSegment)
        }

        return derivativeSegments
    }
    fun derivative(): SegmentedFunction = SegmentedFunction(computeDerivative(1))
    fun secondDerivative():SegmentedFunction = SegmentedFunction(computeDerivative(2))

}

