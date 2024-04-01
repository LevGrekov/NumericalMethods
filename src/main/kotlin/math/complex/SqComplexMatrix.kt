package math.complex

class SqComplexMatrix : ComplexMatrix {
    companion object{
        fun identity(size: Int): SqComplexMatrix = SqComplexMatrix(
            Array(size) { i ->
                Array(size) { j ->
                    if (i == j) ComplexNum(1.0, 0.0) else ComplexNum(0.0, 0.0)
                }
            }
        )
    }
    constructor(data: Array<Array<ComplexNum>>) : super(data) {
        require(data.isNotEmpty() && data.all { it.size == data.size }) {
            "Двумерный массив должен быть квадратным"
        }
    }

    constructor(data: ComplexMatrix) : this(data.data.clone())


    constructor(size: Int) : super(size, size)


    val size: Int
        get() = rows


    val determinant: ComplexNum? by lazy { determinantRecursive() }
    private fun determinantRecursive(): ComplexNum {
        if (this.rows == 2) {
            return this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]
        }

        val det = ComplexNum()
        for (j in 0 until this.cols) {
            val minor = SqComplexMatrix(getMinor(0, j))
            val sign = if (j % 2 == 0) 1.0 else -1.0
            det += this[0, j] * minor.determinantRecursive() * sign
        }
        return det
    }

    fun invertibleMatrix(): ComplexMatrix {
        if (determinant == ComplexNum(0.0, 0.0)) throw Exception("Матрица с Определителем = 0 необратима")
        val result = ComplexMatrix(rows, rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val sign = if ((i + j) % 2.0 == 1.0) -1.0 else 1.0
                val minor = SqComplexMatrix(getMinor(j, i))
                result[i, j] = minor.determinantRecursive() / determinant!! * sign
            }
        }
        return result
    }

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

    operator fun times(other: SqComplexMatrix) = SqComplexMatrix(super.times(other))
}