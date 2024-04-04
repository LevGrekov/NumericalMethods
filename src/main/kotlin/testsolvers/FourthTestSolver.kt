package testsolvers

import math.complex.ComplexMatrix
import math.complex.Complex
import math.complex.SqComplexMatrix
import math.slaumethods.IterationsMethod
import math.slaumethods.SquareRootMethodSLAU


object FourthTestSolver {

    fun firstEx(matrix: SqComplexMatrix, constants: ComplexMatrix) {
        val a = SquareRootMethodSLAU(matrix,constants)
        println(a.solve())
    }

    fun secondEx(matrix: SqComplexMatrix, constants: ComplexMatrix) {
        val a = IterationsMethod()
        println(a.simpleIterations(matrix,constants))
    }

    fun thirdEx(matrixA: SqComplexMatrix, constants: ComplexMatrix) {
        val a = IterationsMethod()
        val tau = 1.0
        val C = SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(),Complex(0.1), Complex(0.1)),
                arrayOf(Complex(0.1), Complex(-0.1), Complex(0.0)),
                arrayOf(Complex(0.2), Complex(-0.2),Complex(0.2))
            )
        )
        val newVars = a.transformToIterativeForm(matrixA,constants,tau,C)
        println(a.solveSeidel(SqComplexMatrix(newVars.first), ComplexMatrix(newVars.second.data).T))
    }

    fun fourth(A: SqComplexMatrix, b: ComplexMatrix) {
        val a = IterationsMethod()
        val tau = -4.1321123
        val C = SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.1),Complex(0.6), Complex(0.3),Complex(0.2)),
                arrayOf(Complex(0.4), Complex(-1.5), Complex(-0.8),Complex(-0.1)),
                arrayOf(Complex(-0.3), Complex(-3.1),Complex(-1.0),Complex(0.0)),
                arrayOf(Complex(-1.3), Complex(-15.6),Complex(-7.8),Complex(-0.3)),
            )
        )
        val C2 = SqComplexMatrix.identity(4)
        val newVars = a.transformToIterativeForm(A,b,tau, C2)
        println(a.solveSeidel(SqComplexMatrix(newVars.first), ComplexMatrix(newVars.second.data).T))
    }

    fun fifith(A: SqComplexMatrix, b: ComplexMatrix) {
        val a = IterationsMethod()
        println(a.solveSeidel2(A,b))
    }

    fun sixth(A: SqComplexMatrix, b: ComplexMatrix){
        val a = IterationsMethod()
        println(a.gradientDescent(A,b,0.00313913,50.6563))
    }

}