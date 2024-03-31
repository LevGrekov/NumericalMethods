package testsolvers

import math.complex.ComplexMatrix
import math.complex.ComplexNum
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

class SquareRootMethodSLAU(val A: ComplexMatrix, val b: ComplexMatrix) {
    fun d(i:Int): Double = when{
        i == 0 -> sign(A[0,0].re)
        i > 0 -> sign(A[0,0].re - (0 until  i).sumOf { k -> s(k,i).abs2() * d(k) })
        else -> 0.0
    }

    fun s(i:Int,j:Int): ComplexNum = when{
        i == 0 && j == 0 -> ComplexNum(sqrt(A[0,0].re/d(0)))
        i == 0 && j != 0 -> (A[0,j]/(s(0,0)*d(0)))
        i == j -> ComplexNum(sqrt(abs(A[i, i].re - (0 until i).sumOf { k -> s(k,i).abs2() * d(k) })),0.0)
        i < j -> {
            val sum = ComplexNum(0.0,0.0)
            for (k in 0 until i) {
                val a = !s(k,i) * s(k,j) * d(k)
                println(a)
                sum += a
            }
            (A[i,j] - sum)/(s(i,i) * d(i))
        }
        else -> ComplexNum(0.0,0.0)
    }

    fun solve(): ComplexMatrix{
        val S = ComplexMatrix(A.rows,A.cols)
        val D = ComplexMatrix(A.rows,A.cols)
        for (i in 0 until A.rows) {
            D[i,i] = ComplexNum(d(i),0.0)
            for (j in 0 until A.cols) {
                S[i,j] = s(i,j)
                println("S[$i,$j] = ${S[i,j]}")
            }
        }
        val X = b * S.conj().invertibleMatrix() * D.invertibleMatrix() * S.invertibleMatrix()
        val y = S.conj() * D
        val a = S.conj() * D * S

        return X
    }
}