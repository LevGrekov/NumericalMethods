package math.complex

class ComplexMatrix(val rows: Int, val cols: Int, private val data: Array<Array<ComplexNum>>) {

    val determinant: ComplexNum? by lazy { determinantRecursive() }
    constructor(rows: Int, cols: Int) : this(rows, cols, Array(rows) { Array<ComplexNum>(cols) { ComplexNum() } })

    operator fun get(i: Int, j: Int): ComplexNum {
        return data[i][j]
    }

    operator fun set(i: Int, j: Int, value: ComplexNum) {
        data[i][j] = value
    }

    operator fun plus(other: ComplexMatrix): ComplexMatrix {
        require(rows == other.rows && cols == other.cols) { "Matrices must have the same dimensions" }
        val result = ComplexMatrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i, j] = this[i, j] + other[i, j]
            }
        }
        return result
    }

    operator fun minus(other: ComplexMatrix): ComplexMatrix {
        require(rows == other.rows && cols == other.cols) { "Matrices must have the same dimensions" }
        val result = ComplexMatrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i, j] = this[i, j] - other[i, j]
            }
        }
        return result
    }

    operator fun times(scalar: Double): ComplexMatrix {
        val result = ComplexMatrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i, j] = this[i, j] * scalar
            }
        }
        return result
    }

    operator fun times(other: ComplexMatrix): ComplexMatrix {
        require(cols == other.rows) { "Number of columns in the first matrix must be equal to the number of rows in the second matrix" }
        val result = ComplexMatrix(rows, other.cols)
        for (i in 0 until rows) {
            for (j in 0 until other.cols) {
                val sum = ComplexNum()
                for (k in 0 until cols) {
                    sum.plusAssign(this[i, k] * other[k, j])
                }
                result[i, j] = sum
            }
        }
        return result
    }

    fun transpose(): ComplexMatrix {
        val result = ComplexMatrix(cols, rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[j, i] = this[i, j]
            }
        }
        return result
    }

    fun conj(): ComplexMatrix {
        val result = ComplexMatrix(cols, rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[j, i] = !this[i, j]
            }
        }
        return result
    }

    fun invertibleMatrix(): ComplexMatrix {
        if (determinant == ComplexNum(0.0,0.0)) throw Exception("Матрица с Определителем = 0 необратима")
        val result = ComplexMatrix(rows,rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val sign = if ((i + j) % 2.0 == 1.0) -1.0 else 1.0
                result[i, j] = getMinor(j,i).determinantRecursive()!!/determinant!! * sign
            }
        }
        return result.transpose()
    }


    private fun determinantRecursive(): ComplexNum? {
        if (this.rows != this.cols) return null
        if (this.rows == 1) {
            return this[0,0]
        }

        if (this.rows == 2) {
            return this[0,0] * this[1,1] - this[0,1] * this[1,0]
        }

        val det = ComplexNum()
        for (j in 0 until this.cols) {
            val minor = this.getMinor(0, j)
            val sign = if (j % 2 == 0) 1.0 else -1.0
            det += this[0,j] * minor.determinantRecursive()!! * sign
        }
        return det
    }


    fun getMinor(row: Int, col: Int): ComplexMatrix {
        val minorData = Array(rows - 1) { Array(cols - 1) { ComplexNum() } }
        var minorRow = 0
        for (i in 0 until rows) {
            if (i == row) continue
            var minorCol = 0
            for (j in 0 until cols) {
                if (j == col) continue
                minorData[minorRow][minorCol] = data[i][j]
                minorCol++
            }
            minorRow++
        }
        return ComplexMatrix(rows - 1, cols - 1, minorData)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                builder.append("${this[i, j]} ")
            }
            builder.append("\n")
        }
        return builder.toString()
    }
}