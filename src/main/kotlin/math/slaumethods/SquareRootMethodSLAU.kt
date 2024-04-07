package math.slaumethods

import math.complex.ComplexMatrix
import math.complex.Complex
import math.complex.SqComplexMatrix
import math.neq
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

class SquareRootMethodSLAU(val A: SqComplexMatrix, val b: ComplexMatrix) {

    private val S = SqComplexMatrix(A.size)
    private val D = SqComplexMatrix(A.size)


    private fun findD(i:Int): Double = when{
        i == 0 -> sign(A[0,0].re)
        i > 0 -> sign(A[0,0].re - (0 until  i).sumOf { k -> findS(k,i).abs2() * findD(k) })
        else -> 0.0
    }

    private fun findS(i:Int, j:Int): Complex = when {
        i == 0 && j == 0 -> Complex(sqrt(A[0,0].re/D[0,0].re))
        i == 0 && j != 0 -> (A[0,j]/(findS(0,0)*D[0,0].re))
        i == j -> Complex(sqrt(abs(A[i, i].re - (0 until i).sumOf { k -> S[k,i].abs2() * D[k,k].re })),0.0)
        i < j -> {
            val sum = Complex(0.0,0.0)
            for (k in 0 until i) {
                val a = !S[k,i] * S[k,j] * D[k,k].re
                sum += a
            }
            (A[i,j] - sum)/(S[i,i] * D[0,0].re)
        }
        else -> Complex(0.0,0.0)
    }

    fun solve(): ComplexMatrix{
        if(!A.isSymmetric()){
            throw Exception("Метод Квадратных Корней. Матрица не симметрична")
        }
        for (i in 0 until A.rows) {
            if(A[i,i].im neq 0.0){
                throw Exception("Метод Квадратных Корней. Матрица должна иметь на главной диагонале только действительные числа")
            }
        }
        for (i in 0 until A.rows) {
            D[i,i] = Complex(findD(i),0.0)
            for (j in 0 until A.cols) {
                S[i,j] = findS(i,j)
            }
        }
        val X =  (S.H*D*S).Inv * b
        return X
    }
}