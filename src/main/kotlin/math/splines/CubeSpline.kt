package math.splines

import SystemSolver
import math.polynomials.Polynomial
import kotlin.math.pow

class CubeSpline (points: Map<Double, Double>) : InterpolationSpline(points) {

    override val segments: List<SplineSegment> = buildSegments()

    private fun buildSegments(): List<SplineSegment> {
        val coefficients = getCoefficients()
        val segments = mutableListOf<SplineSegment>()
        val iterator = points.iterator()

        coefficients?.let {
            if (iterator.hasNext()) {
                var (x1, y1) = iterator.next().toPair()
                var segmentIndex = 0

                while (iterator.hasNext()) {
                    val (x2, y2) = iterator.next().toPair()

                    val polynomial = Polynomial(
                        coefficients[4 * segmentIndex+3],
                        coefficients[4 * segmentIndex+2],
                        coefficients[4 * segmentIndex+1],
                        coefficients[4 * segmentIndex])
                    segments.add(SplineSegment(x1, x2, polynomial))

                    x1 = x2
                    y1 = y2
                    segmentIndex++
                }
            }
        }
        return segments
    }
    private fun getCoefficients(): DoubleArray?{
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

         return SystemSolver.gaussMethod(matrix,constants)
    }

    private fun insertVector(originalVector: DoubleArray, insertVector: DoubleArray, position: Int): DoubleArray {
        require(position >= 0 && position + insertVector.size <= originalVector.size) {
            "Позиция вставки выходит за пределы размера исходного вектора."
        }
        val resultVector = originalVector.copyOf()
        System.arraycopy(insertVector, 0, resultVector, position, insertVector.size)
        return resultVector
    }

}

