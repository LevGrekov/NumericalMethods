package testsolvers

import math.complex.ComplexMatrix
import math.complex.ComplexNum
import math.complex.SqComplexMatrix
import math.slaumethods.IterationsMethod
import math.slaumethods.SquareRootMethodSLAU


object FourthTestSolver {
    @OptIn(ExperimentalStdlibApi::class)
    fun firstEx(matrix: SqComplexMatrix, constants: ComplexMatrix) {
        val a = SquareRootMethodSLAU(matrix,constants)
        println(a.solve())
    }

    fun secondEx(matrix: SqComplexMatrix, constants: ComplexMatrix) {
        val a = IterationsMethod()
        println(a.solve(matrix,constants))
    }

    fun thirdEx(matrixA: SqComplexMatrix, constants: ComplexMatrix) {
        val a = IterationsMethod()
        val tau = 1.0
        val C = SqComplexMatrix(
            arrayOf(
                arrayOf(ComplexNum(),ComplexNum(0.1), ComplexNum(0.1)),
                arrayOf(ComplexNum(0.1), ComplexNum(-0.1), ComplexNum(0.0)),
                arrayOf(ComplexNum(0.2), ComplexNum(-0.2),ComplexNum(0.2))
            )
        )
        val newVars = a.transformToIterativeForm(matrixA,constants,tau,C)
        println(a.solveSeidel(SqComplexMatrix(newVars.first), ComplexMatrix(newVars.second.data).transpose()))
    }

}