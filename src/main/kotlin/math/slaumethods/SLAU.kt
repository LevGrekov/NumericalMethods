package math.slaumethods

import math.complex.Complex
import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import utils.FileWriter
import kotlin.math.*

class SLAU(matrix: SqComplexMatrix,constants: ComplexMatrix, var tolerance: Double = 1e-3){

    companion object{
        fun complexVectorNorm(vector: ComplexMatrix): Double {
            if(vector.cols != 1) throw Exception("В complexVectorNorm попала Строка ")
            var sum = 0.0
            for (i in 0 until vector.rows) {
                sum += vector[i,0].abs2()
            }
            return sqrt(sum)
        }

        fun transformToIterativeForm(A: SqComplexMatrix, b: ComplexMatrix,tau:Double,C:SqComplexMatrix) =
            Pair(A.E - (C * tau) * A, C*b * tau)

        fun copyElementsFromMatrix (B: SqComplexMatrix, func: (Int, Int) -> Boolean) : SqComplexMatrix {
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
        fun relativeError(exactX:ComplexMatrix,approximateX:ComplexMatrix) =
            findAbsError(exactX,approximateX)/ complexVectorNorm(approximateX)

    }

    val _matrix: SqComplexMatrix
    val _constants: ComplexMatrix


    init {
        this._matrix = matrix
        this._constants = constants
    }

    fun findResidual(xComputed:ComplexMatrix) = complexVectorNorm(_constants-_matrix * xComputed)

    fun simpleIterations(tau:Double? = null, C:SqComplexMatrix? = null) = when{
        tau == null && C == null -> simpleIterations(_matrix, _constants)
        else -> {
            val newSLAU = transformToIterativeForm(_matrix, _constants, tau ?: 1.0, C ?: _matrix.E)
            simpleIterations(newSLAU.first,newSLAU.second)
        }
    }

    private fun simpleIterations(A:SqComplexMatrix, b:ComplexMatrix): ComplexMatrix {
        var xOld = ComplexMatrix(A.size,1 )
        var xNew = A*xOld + b
        val x1x0norm = complexVectorNorm((xNew - xOld))
        val q1 = A.norm()
        val qinf = A.norm(true)
        val q = min(q1,qinf)
        if (q >= 1){
            throw Exception("Ошибка Простых Итераций. Норма больше Еденицы: q1=$q1 qinf = $qinf")
        }
        val k = ceil(ln(((1-q)*tolerance)/x1x0norm)/ ln(q)).toInt()

        FileWriter.write("norm: $q")
        FileWriter.write("irerations: $k")

        repeat(k){
            xNew = A*xOld + b
            xOld = xNew
            if (it < 5 || it > k - 5) {
                FileWriter.write("x${it + 1}:\n$xNew")
            }
        }

        return xNew
    }

    fun solveSeidel(tau:Double? = null, C:SqComplexMatrix? = null): ComplexMatrix {
        val newSLAU = when{
            tau == null && C == null -> _matrix to _constants
            else ->  transformToIterativeForm(_matrix, _constants, tau ?: 1.0, C ?: _matrix.E)
        }

        val H = copyElementsFromMatrix(newSLAU.first) { i, j -> j < i }
        val F = copyElementsFromMatrix(newSLAU.first) { i, j -> j >= i }
        val EminHinv = (newSLAU.first.E - H).Inv
        val BTilda = EminHinv * F
        val cTilda = EminHinv * newSLAU.second
        return simpleIterations(BTilda, cTilda)
    }

    fun solveSeidel2(Ct:SqComplexMatrix? = null): ComplexMatrix {
        val newSLAU = when{
            Ct == null -> _matrix to _constants
            else -> Ct * _matrix to Ct * _constants
        }
        println(newSLAU.first.norm())
        val B = copyElementsFromMatrix(newSLAU.first) { i, j -> i >= j }
        val C = copyElementsFromMatrix(newSLAU.first) { i, j -> i < j }
        val tildaB =  -B.Inv*C
        val newC = (B.Inv * newSLAU.second)

        return simpleIterations(tildaB,newC)
    }

    fun gradientDescent(minEigen:Double, maxEigen:Double) : ComplexMatrix{
        val (M,c) = when(_matrix.isSymmetric()){
            true-> _matrix to _constants
            false-> _matrix.T * _matrix to _matrix.T * _constants
        }
        val n = M.size

        var xOld = ComplexMatrix(n,1 )
        var r = c - M*xOld
        var tau = (r.T * r).sum()/ (r.T * ((M * r)) ).sum()
        var xNew = xOld + r * tau.re

        val scaledNorm = complexVectorNorm(r) / minEigen
        val rangeRatio = (maxEigen-minEigen)/(maxEigen+minEigen)
        val k = ceil(ln(tolerance/scaledNorm)/ln(rangeRatio)).toInt()
        FileWriter.write("iterations: $k")

        repeat(k-1){
            r = c - M*xOld
            tau = (r.T * r).sum()/ (r.T * ((M * r)) ).sum()
            xNew = xOld + r * tau.re
            xOld = xNew
            if (it < 5 || it > k - 5) {
                FileWriter.write("x${it + 1}:\n $xNew")
            }
        }
        return xNew
    }

    fun solveSquareRootsMethod(): ComplexMatrix = SquareRootMethodSLAU( _matrix, _constants).solve()

    fun gaussianEliminationPartialPivoting(): ComplexMatrix {
        val M = _matrix.copy()
        val c = _constants.copy()
        val n = M.rows

        // Прямой ход
        for (i in 0 until n) {
            // Выбор ведущего элемента
            var maxRow = i
            for (k in i + 1 until n) {
                if (M[k,i].abs() > M[maxRow,i].abs()) {
                    maxRow = k
                }
            }
            M.swapRows(i, maxRow) // Обмен строк
            val temp = c[i,0]
            c[i,0] = c[maxRow,0]
            c[maxRow,0] = temp

            // Обнуление элементов под главной диагональю
            for (j in i + 1 until n) {
                val factor = M[j,i] / M[i,i]
                for (k in i until n) {
                    M[j,k].minusAssign(factor * M[i,k])
                }
                c[j,0].minusAssign(factor * c[i,0])
            }
        }

        // Обратный ход
        val x = Array(n) { Complex(0.0, 0.0) }
        for (i in n - 1 downTo 0) {
            x[i] = c[i,0]
            for (j in i + 1 until n) {
                x[i].minusAssign(M[i,j] * x[j])
            }
            x[i].divAssign(M[i,i])
        }

        return arrayToComplexMatrix(x)
    }
}