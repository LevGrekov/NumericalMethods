package math.slaumethods

import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import kotlin.math.min
import kotlin.math.pow

class IterationsMethod {
    fun solve(B: SqComplexMatrix,c: ComplexMatrix,tolerance : Double = 1e-3): ComplexMatrix {

        var xOld = ComplexMatrix(B.size,1 )
        var xnew = B*xOld + c.transpose()
        val x1x0norm = complexVectorNorm((xnew - xOld).transpose().data[0])
        val q = min(B.norm(),B.norm(true))
        var k = 0
        while (q.pow(k) * x1x0norm / (1-q) >= tolerance){
            xnew = B*xOld + c.transpose()
            xOld = xnew
            k++
        }
        println(k)
        println(B.norm(true))

        return xnew
    }
    fun transformToIterativeForm(A: SqComplexMatrix, b: ComplexMatrix,tau:Double,C:SqComplexMatrix) =
        Pair(SqComplexMatrix.identity(A.size) - (C * tau) * A,C*(b.transpose())*tau)


    fun splitMatrix(B: SqComplexMatrix): Pair<SqComplexMatrix, SqComplexMatrix> {
        val n = B.size
        val H = SqComplexMatrix(B.size)
        val F = SqComplexMatrix(B.size)

        for (i in 0 until n) {
            for (j in 0 until n) {
                if (j < i) {
                    H[i,j] = B[i,j]
                } else {
                    F[i,j] = B[i,j]
                }
            }
        }
        return Pair(H, F)
    }
    fun solveSeidel(B: SqComplexMatrix,c: ComplexMatrix,tolerance : Double = 1e-3): ComplexMatrix{
        val (H,F) = splitMatrix(B)
        val EminHinv = SqComplexMatrix(SqComplexMatrix.identity(B.size) - H).invertibleMatrix()
        val BTilda = EminHinv * F
        val cTilda = (EminHinv * c.transpose()).transpose()
        return solve(SqComplexMatrix(BTilda),cTilda,tolerance)
    }
}
