package testsolvers

import math.complex.ComplexMatrix
import math.complex.ComplexNum


object FourthTestSolver {
    @OptIn(ExperimentalStdlibApi::class)
    fun firstEx(matrix: ComplexMatrix, constants: ComplexMatrix) {
        val a = SquareRootMethodSLAU(matrix,constants)
        println(a.solve())
    }

}