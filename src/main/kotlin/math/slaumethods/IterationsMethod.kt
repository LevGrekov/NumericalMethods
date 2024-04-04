package math.slaumethods

import math.abs
import math.complex.Complex
import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

class IterationsMethod(private val tolerance: Double = 1e-4) {
    fun simpleIterations(B: SqComplexMatrix, c: ComplexMatrix): ComplexMatrix? {

        var xOld = ComplexMatrix(B.size,1 )
        var xnew = B*xOld + c.T
        val x1x0norm = complexVectorNorm((xnew - xOld).T.data[0])
        val q1 = B.norm()
        val qinf = B.norm(true)
        val q = min(q1,qinf)
        if(q>=1){
            println("Норма больше Еденицы: q1=$q1 qinf = $qinf" )
            return null
        }
        var k = 0
        while (q.pow(k) * x1x0norm / (1-q) >= tolerance){
            xnew = B*xOld + c.T
            xOld = xnew
            k++
        }
        println(k)
        println(B.norm(true))

        return xnew
    }
    fun transformToIterativeForm(A: SqComplexMatrix, b: ComplexMatrix,tau:Double,C:SqComplexMatrix) =
        Pair(SqComplexMatrix.identity(A.size) - (C * tau) * A,C*(b.T)*tau)


    private fun copyElementsFromMatrix (B: SqComplexMatrix, func: (Int, Int) -> Boolean) : SqComplexMatrix {
        val n = B.size
        val M = SqComplexMatrix(n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (func(i,j)) {
                    M[i,j] = B[i,j]
                }
            }
        }
        return M
    }

    fun solveSeidel(B: SqComplexMatrix,c: ComplexMatrix): ComplexMatrix?{
        val H = copyElementsFromMatrix(B) { i, j -> j < i }
        val F = copyElementsFromMatrix(B) { i, j -> j >= i }
        val EminHinv = SqComplexMatrix(SqComplexMatrix.identity(B.size) - H).invertibleMatrix()
        val BTilda = EminHinv * F
        val cTilda = (EminHinv * c.T).T
        return simpleIterations(SqComplexMatrix(BTilda),cTilda)
    }

    fun solveSeidel2(A: SqComplexMatrix,b: ComplexMatrix): ComplexMatrix? {
        val B = copyElementsFromMatrix(A) { i, j -> i >= j }
        val C = copyElementsFromMatrix(A) { i, j -> i < j }
        println(-(B.Inv * C))
        val BTilda =  SqComplexMatrix((B.Inv*C) * -1.0)
        val c = (B.Inv * b.T).T

        return simpleIterations(BTilda,c)
    }

    fun gradientDescent(matrixA: SqComplexMatrix, constants: ComplexMatrix,m:Double,M:Double) : ComplexMatrix{
        val n = matrixA.size
        val newA = matrixA.H * matrixA
        val newb = matrixA.H * constants.T

        var xOld = ComplexMatrix(n,1 )
        var r = newb - newA*xOld
        var tau = (r.T * r).sum()/ (r.T * ((newA * r)) ).sum()
        var xNew = xOld + r * tau.re

        val scaledNorm = complexVectorNorm(r.getArray()!!) / m
        val rangeRatio = (M-m)/(M+m)

        var k = 0
        while (scaledNorm * rangeRatio.pow(k) >= tolerance){
            k++
        }

        println(k)

        repeat(k-1){
            r = newb - newA*xOld
            tau = (r.T * r).sum()/ (r.T * ((newA * r)) ).sum()
            xNew = xOld + r * tau.re
            xOld = xNew
        }
        return xNew
    }

    fun gaussianEliminationPartialPivoting(A: ComplexMatrix, b: Array<Complex>): Array<Complex> {
        val n = b.size

        // Прямой ход
        for (i in 0 until n) {
            // Выбор ведущего элемента
            var maxRow = i
            for (k in i + 1 until n) {
                if (A[k,i].abs() > A[maxRow,i].abs()) {
                    maxRow = k
                }
            }
            A.swapRows(i, maxRow) // Обмен строк
            val temp = b[i]
            b[i] = b[maxRow]
            b[maxRow] = temp

            // Обнуление элементов под главной диагональю
            for (j in i + 1 until n) {
                val factor = A[j,i] / A[i,i]
                for (k in i until n) {
                    A[j,k].minusAssign(factor * A[i,k])
                }
                b[j].minusAssign(factor * b[i])
            }
        }

        // Обратный ход
        val x = Array(n) { Complex(0.0, 0.0) }
        for (i in n - 1 downTo 0) {
            x[i] = b[i]
            for (j in i + 1 until n) {
                x[i].minusAssign(A[i,j] * x[j])
            }
            x[i].divAssign(A[i,i])
        }

        return x
    }
}
