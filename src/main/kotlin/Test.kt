fun main() {
    val A = arrayOf(
        doubleArrayOf(0.18, 2.11, 0.13, -0.22),
        doubleArrayOf(0.33, -0.22, -1.0, 0.17),
        doubleArrayOf(-1.0, 0.11, 2.0, -0.45),
        doubleArrayOf(7.0, -0.17, -0.22, 0.33)
    )
    val y = doubleArrayOf(0.22, 0.11, 1.0, 0.21)

    // Меняем местами строки 1 и 4
    for (i in A[0].indices) {
        val temp = A[0][i]
        A[0][i] = A[3][i]
        A[3][i] = temp
    }
    val temp = y[0]
    y[0] = y[3]
    y[3] = temp

    // Меняем местами строки 2 и 4
    for (i in A[1].indices) {
        val temp = A[1][i]
        A[1][i] = A[3][i]
        A[3][i] = temp
    }
    val temp2 = y[1]
    y[1] = y[3]
    y[3] = temp2

    // Делаем линейное преобразование
    for (i in A[3].indices) {
        A[3][i] += 0.02 * A[0][i] + 0.08 * A[1][i] + 0.5 * A[2][i]
    }
    y[3] += 0.02 * y[0] + 0.08 * y[1] + 0.5 * y[2]

    // Вывод результатов
    println("Матрица A после преобразования:")
    for (row in A) {
        for (elem in row) {
            print("$elem\t")
        }
        println()
    }
    println("Массив y после преобразования:")
    for (elem in y) {
        print("$elem\t")
    }
}