package math.complex

open class ComplexMatrix(protected val _data: Array<Array<ComplexNum>>) {
    constructor(rows: Int, cols: Int) : this(Array(rows) { Array(cols) { ComplexNum() } })

    val data get() = _data

    val rows: Int
        get() = _data.size

    val cols: Int
        get() = if (_data.isNotEmpty()) _data[0].size else 0

    operator fun get(i: Int, j: Int): ComplexNum {
        return _data[i][j]
    }

    operator fun set(i: Int, j: Int, value: ComplexNum) {
        _data[i][j] = value
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

    fun getMinor(row: Int, col: Int): ComplexMatrix {
        val minorData = Array(rows - 1) { Array(cols - 1) { ComplexNum() } }
        var minorRow = 0
        for (i in 0 until rows) {
            if (i == row) continue
            var minorCol = 0
            for (j in 0 until cols) {
                if (j == col) continue
                minorData[minorRow][minorCol] = _data[i][j]
                minorCol++
            }
            minorRow++
        }
        return ComplexMatrix(minorData)
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