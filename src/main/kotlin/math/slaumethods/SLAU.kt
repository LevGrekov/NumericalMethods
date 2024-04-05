package math.slaumethods

import math.complex.Complex
import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import kotlin.math.min
import kotlin.math.pow

class SLAU(matrix: SqComplexMatrix,constants: Array<Complex>, var tolerance: Double = 10e-5){

    companion object{
        fun transformToIterativeForm(A: SqComplexMatrix, b: ComplexMatrix,tau:Double,C:SqComplexMatrix) =
            SLAU(A.E - (C * tau) * A, (C*b * tau).getArray())

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

        private fun arrayToComplexMatrix(array: Array<Complex>): ComplexMatrix {
            val matrix = arrayOf(array)
            return ComplexMatrix(matrix).T
        }

        fun findAbsError(exactX:ComplexMatrix,approximateX:ComplexMatrix ) =
            complexVectorNorm((exactX - approximateX))
    }

    val matrix: SqComplexMatrix
    val constants: ComplexMatrix

    init {
        this.matrix = matrix
        this.constants = arrayToComplexMatrix(constants)
    }

    private fun simpleIterations(A:SqComplexMatrix, b:ComplexMatrix): ComplexMatrix {
        var xOld = ComplexMatrix(A.size,1 )
        var xnew = A*xOld + b
        val x1x0norm = complexVectorNorm((xnew - xOld))
        val q1 = A.norm()
        val qinf = A.norm(true)
        val q = min(q1,qinf)
        if (q >= 1){
            throw Exception("Ошибка Простых Итераций. Норма больше Еденицы: q1=$q1 qinf = $qinf")
        }
        var k = 0
        while (q.pow(k) * x1x0norm / (1-q) >= tolerance){
            xnew = A*xOld + b
            xOld = xnew
            k++
        }
        println(k)
        println(A.norm())

        return xnew
    }

    fun simpleIterations(tau:Double? = null, C:SqComplexMatrix? = null) = when{
        tau == null && C == null ->{
            simpleIterations(matrix,constants)
        }
        else -> {
            val newSLAU = transformToIterativeForm(matrix,constants,tau ?: 1.0,C ?: matrix.E)
            simpleIterations(newSLAU.matrix,newSLAU.constants)
        }
    }

    fun solveSeidel(tau:Double? = null, C:SqComplexMatrix? = null): ComplexMatrix{
        val SLAU = when{
            tau == null && C == null -> this
            else -> transformToIterativeForm(matrix,constants,tau ?: 1.0,C ?: matrix.E)
        }

        val H = copyElementsFromMatrix(SLAU.matrix) { i, j -> j < i }
        val F = copyElementsFromMatrix(SLAU.matrix) { i, j -> j >= i }
        val EminHinv = (SLAU.matrix.E - H).Inv
        val BTilda = EminHinv * F
        val cTilda = EminHinv * SLAU.constants
        return simpleIterations(BTilda,cTilda)
    }

    fun solveSeidel2(): ComplexMatrix {
        if(!matrix.isDiagonallyDominant()) {
            throw Exception("Зейдель II. Матрица не имеет диагонального преобладания")
        }
        val B = copyElementsFromMatrix(matrix) { i, j -> i >= j }
        val C = copyElementsFromMatrix(matrix) { i, j -> i < j }
        val BTilda =  -B.Inv*C
        val c = (B.Inv * constants)

        return simpleIterations(BTilda,c)
    }

    fun gradientDescent(m:Double, M:Double) : ComplexMatrix{
        val (matrix,constants) = when(matrix.isSymmetric()){
            true-> this.matrix to this.constants
            false-> matrix.H * matrix to matrix.H * constants
        }
        val n = matrix.size

        var xOld = ComplexMatrix(n,1 )
        var r = constants - matrix*xOld
        var tau = (r.T * r).sum()/ (r.T * (matrix * r) ).sum() //Скалярное произведение
        var xNew = xOld + r * tau.re

        val scaledNorm = complexVectorNorm(r) / m
        val rangeRatio = (M-m)/(M+m)

        var k = 0
        while (scaledNorm * rangeRatio.pow(k) >= tolerance){
            k++
        }

        println(k)

        repeat(k-1){
            r = constants - matrix*xOld
            tau = (r.T * r).sum()/ (r.T * ((matrix * r)) ).sum()
            xNew = xOld + r * tau.re
            xOld = xNew
            println(xNew)
        }
        return xNew
    }

    fun solveSquareRootsMethod(): ComplexMatrix = SquareRootMethodSLAU( matrix, constants).solve()

    fun gaussianEliminationPartialPivoting(): ComplexMatrix {
        val n = matrix.size

        // Прямой ход
        for (i in 0 until n) {
            // Выбор ведущего элемента
            var maxRow = i
            for (k in i + 1 until n) {
                if (matrix[k,i].abs() > matrix[maxRow,i].abs()) {
                    maxRow = k
                }
            }
            matrix.swapRows(i, maxRow) // Обмен строк
            val temp = constants[i,0]
            constants[i,0] = constants[maxRow,0]
            constants[maxRow,0] = temp

            // Обнуление элементов под главной диагональю
            for (j in i + 1 until n) {
                val factor = matrix[j,i] / matrix[i,i]
                for (k in i until n) {
                    matrix[j,k].minusAssign(factor * matrix[i,k])
                }
                constants[j,0].minusAssign(factor * constants[i,0])
            }
        }

        // Обратный ход
        val x = Array(n) { Complex(0.0, 0.0) }
        for (i in n - 1 downTo 0) {
            x[i] = constants[i,0]
            for (j in i + 1 until n) {
                x[i].minusAssign(matrix[i,j] * x[j])
            }
            x[i].divAssign(matrix[i,i])
        }

        return arrayToComplexMatrix(x)
    }
}