package math.complex

import math.abs

class SqComplexMatrix : ComplexMatrix {
    val determinant: Complex by lazy { determinantRecursive() }
    val Inv by lazy { invertibleMatrix() }
    override val T by lazy { SqComplexMatrix(super.T) }
    override val H by lazy { SqComplexMatrix(super.H) }
    val E by lazy { identity(size) }
    val size: Int
        get() = rows
    constructor(data: Array<Array<Complex>>) : super(data) {
        require(data.isNotEmpty() && data.all { it.size == data.size }) {
            "Двумерный массив должен быть квадратным"
        }
    }
    constructor(matrix: ComplexMatrix) : this(matrix.data)

    constructor(size: Int) : super(size, size)

    private fun determinantRecursive(): Complex {
        if (this.rows == 2) {
            return this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]
        }

        val det = Complex()
        for (j in 0 until this.cols) {
            val minor = SqComplexMatrix(getMinor(0, j))
            val sign = if (j % 2 == 0) 1.0 else -1.0
            det += this[0, j] * minor.determinantRecursive() * sign
        }
        return det
    }

    private fun invertibleMatrix(): SqComplexMatrix {
        if (determinant == Complex(0.0, 0.0)) throw Exception("Матрица с Определителем = 0 необратима")
        val result = SqComplexMatrix(rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val sign = if ((i + j) % 2.0 == 1.0) -1.0 else 1.0
                val minor = SqComplexMatrix(getMinor(j, i))
                result[i, j] = minor.determinantRecursive() / determinant * sign
            }
        }
        return result
    }
    private fun identity(size: Int): SqComplexMatrix = SqComplexMatrix(
        Array(size) { i ->
            Array(size) { j ->
                if (i == j) Complex(1.0, 0.0) else Complex(0.0, 0.0)
            }
        }
    )

    fun calculateTrace(): Complex =
        (0 until size).fold(Complex()) { acc, i -> acc + this[i,i] }

    fun norm(inf:Boolean = false): Double {
        var maxSum = 0.0
        for (i in 0 until size) {
            var sum = 0.0
            for (j in 0 until size) {
                sum += if (inf) this[i,j].abs() else this[j,i].abs()
            }
            if (sum > maxSum) {
                maxSum = sum
            }
        }
        return maxSum
    }

    fun isSymmetric(): Boolean {
        val n = size
        for (i in 0 until n) {
            for (j in 0 until i) {
                if (this[i,j] neq this[j,i]) {
                    return false
                }
            }
        }
        return true
    }
    fun isDiagonallyDominant(): Boolean {
        for (i in 0 until size) {
            var diagonalSum = 0.0
            for (j in 0 until size) {
                if (i != j) {
                    diagonalSum += abs(this[i,j])
                }
            }
            if (abs(this[i,i]) <= diagonalSum) {
                return false
            }
        }
        return true
    }

    override fun copy() = SqComplexMatrix(super.copy())
    operator fun times(other: SqComplexMatrix) = SqComplexMatrix(super.times(other))
    override operator fun times(scalar:Double) = SqComplexMatrix(super.times(scalar))
    operator fun plus(other: SqComplexMatrix) = SqComplexMatrix(super.plus(other))
    operator fun minus(other: SqComplexMatrix) = SqComplexMatrix(super.minus(other))
    override operator fun unaryMinus() = SqComplexMatrix(super.unaryMinus())
}