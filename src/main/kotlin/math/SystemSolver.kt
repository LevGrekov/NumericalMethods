object SystemSolver {
    @OptIn(ExperimentalStdlibApi::class)
    fun cramerMethod(coefficients: Array<DoubleArray>, constants: DoubleArray): DoubleArray {
        val n = constants.size
        val solution = DoubleArray(n)

        val determinantA = calculateDeterminant(coefficients)

        for (i in 0..<n) {
            val modifiedCoefficients = Array(n) { DoubleArray(n) }
            for (j in 0..<n) {
                for (k in 0..<n) {
                    modifiedCoefficients[j][k] = coefficients[j][k]
                    if (k == i) {
                        modifiedCoefficients[j][k] = constants[j]
                    }
                }
            }

            val determinantModified = calculateDeterminant(modifiedCoefficients)

            solution[i] = determinantModified / determinantA
        }

        return solution
    }
    @OptIn(ExperimentalStdlibApi::class)
    private fun calculateDeterminant(matrix: Array<DoubleArray>): Double {
        val n = matrix.size

        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        }

        var determinant = 0.0

        for (i in 0..<n) {
            val subMatrix = Array(n - 1) { DoubleArray(n - 1) }
            for (j in 1..<n) {
                for (k in 0..<n) {
                    if (k < i) {
                        subMatrix[j - 1][k] = matrix[j][k]
                    } else if (k > i) {
                        subMatrix[j - 1][k - 1] = matrix[j][k]
                    }
                }
            }
            determinant += matrix[0][i] * calculateDeterminant(subMatrix) * if (i % 2 == 0) 1 else -1
        }
        return determinant
    }

    fun gaussMethod(coefficients: Array<DoubleArray>, constants: DoubleArray): DoubleArray? {
        val n = constants.size

        // Прямой ход метода Гаусса с выбором главного элемента
        for (i in 0 until n) {
            // Поиск максимального элемента в столбце i
            var maxIndex = i
            for (k in i + 1 until n) {
                if (Math.abs(coefficients[k][i]) > Math.abs(coefficients[maxIndex][i])) {
                    maxIndex = k
                }
            }

            // Обмен строк i и maxIndex
            val tempCoefficients = coefficients[i]
            coefficients[i] = coefficients[maxIndex]
            coefficients[maxIndex] = tempCoefficients

            val tempConstant = constants[i]
            constants[i] = constants[maxIndex]
            constants[maxIndex] = tempConstant

            // Нормализация строки i
            val pivot = coefficients[i][i]
            if (pivot == 0.0) {
                // Обработка ситуации, когда опорный элемент равен нулю
                return null
            }

            for (j in i until n) {
                coefficients[i][j] /= pivot
            }
            constants[i] /= pivot

            // Вычитание i-й строки из остальных строк
            for (k in 0 until n) {
                if (k != i) {
                    val factor = coefficients[k][i]
                    for (j in i until n) {
                        coefficients[k][j] -= factor * coefficients[i][j]
                    }
                    constants[k] -= factor * constants[i]
                }
            }
        }

        // Обратный ход метода Гаусса
        val solution = DoubleArray(n)
        for (i in n - 1 downTo 0) {
            solution[i] = constants[i]
            for (j in i + 1 until n) {
                solution[i] -= coefficients[i][j] * solution[j]
            }
        }

        return solution
    }

    fun solveTridiagonalMatrixAkaThomasMethod(matrix: Array<DoubleArray>, constants: DoubleArray): DoubleArray {
        val n = matrix.size

        val a = DoubleArray(n)
        val b = DoubleArray(n)
        val c = DoubleArray(n)

        // Инициализация коэффициентов
        for (i in 0 until n) {
            a[i] = matrix[i][if (i > 0) i - 1 else i]
            b[i] = matrix[i][i]
            c[i] = matrix[i][if (i < n - 1) i + 1 else i]
        }

        // Прямой ход метода прогонки
        for (i in 1 until n) {
            val m = a[i] / b[i - 1]
            b[i] -= m * c[i - 1]
            constants[i] -= m * constants[i - 1]
        }

        // Обратный ход метода прогонки
        val x = DoubleArray(n)
        x[n - 1] = constants[n - 1] / b[n - 1]

        for (i in n - 2 downTo 0) {
            x[i] = (constants[i] - c[i] * x[i + 1]) / b[i]
        }

        return x
    }

    fun printMatrix(matrix: Array<DoubleArray>) {
        for (row in matrix) {
            for (element in row) {
                print("$element ")
            }
            println()
        }
    }
    fun printMatrix(matrix: DoubleArray) {
        for (element in matrix) {
            print("$element ")
        }
    }
}