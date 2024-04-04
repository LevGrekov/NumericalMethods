package math.slaumethods

import math.complex.ComplexMatrix
import math.complex.Complex
import math.complex.SqComplexMatrix
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

class SquareRootMethodSLAU(val A: SqComplexMatrix, val b: ComplexMatrix) {

    private val s = SqComplexMatrix(A.size)
    private val d = SqComplexMatrix(A.size)


    private fun findD(i:Int): Double = when{
        i == 0 -> sign(A[0,0].re)
        i > 0 -> sign(A[0,0].re - (0 until  i).sumOf { k -> findS(k,i).abs2() * findD(k) })
        else -> 0.0
    }

    private fun findS(i:Int, j:Int): Complex = when {
        i == 0 && j == 0 -> Complex(sqrt(A[0,0].re/d[0,0].re))
        i == 0 && j != 0 -> (A[0,j]/(findS(0,0)*d[0,0].re))
        i == j -> Complex(sqrt(abs(A[i, i].re - (0 until i).sumOf { k -> s[k,i].abs2() * d[k,k].re })),0.0)
        i < j -> {
            val sum = Complex(0.0,0.0)
            for (k in 0 until i) {
                val a = !s[k,i] * s[k,j] * d[k,k].re
                sum += a
            }
            (A[i,j] - sum)/(s[i,i] * d[0,0].re)
        }
        else -> Complex(0.0,0.0)
    }

    fun solve(): ComplexMatrix{
        for (i in 0 until A.rows) {
            d[i,i] = Complex(findD(i),0.0)
            for (j in 0 until A.cols) {
                s[i,j] = findS(i,j)
            }
        }
        val X = b * SqComplexMatrix(( s.conj() * d )).invertibleMatrix() * s.invertibleMatrix()
        val y = s.conj() * d
        val a = s.conj() * d * s
        return X
    }

}