package math.slaumethods

import math.complex.Complex
import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import kotlin.math.min
import kotlin.math.pow

class SLAU(val matrix: SquareRootMethodSLAU,val constants: Array<Complex>){

    companion object{
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

    }

    var tolerance = 10e-4
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

    fun solveSeidel(B: SqComplexMatrix,c: ComplexMatrix): ComplexMatrix?{
        val H = copyElementsFromMatrix(B) { i, j -> j < i }
        val F = copyElementsFromMatrix(B) { i, j -> j >= i }
        val EminHinv = SqComplexMatrix(SqComplexMatrix.identity(B.size) - H).invertibleMatrix()
        val BTilda = EminHinv * F
        val cTilda = (EminHinv * c.T).T
        return simpleIterations(SqComplexMatrix(BTilda),cTilda)
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

    fun transformToIterativeForm(A: SqComplexMatrix, b: ComplexMatrix,tau:Double,C:SqComplexMatrix) =
        Pair(SqComplexMatrix.identity(A.size) - (C * tau) * A,C*(b.T)*tau)

    fun solveSeidel2(A: SqComplexMatrix,b: ComplexMatrix): ComplexMatrix? {
        val B = copyElementsFromMatrix(A) { i, j -> i >= j }
        val C = copyElementsFromMatrix(A) { i, j -> i < j }
        println(-(B.Inv * C))
        val BTilda =  SqComplexMatrix((B.Inv*C) * -1.0)
        val c = (B.Inv * b.T).T

        return simpleIterations(BTilda,c)
    }

}