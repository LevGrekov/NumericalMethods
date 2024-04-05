package math.slaumethods

import math.complex.ComplexMatrix
import math.complex.Complex
import kotlin.math.sqrt

fun residual(A: ComplexMatrix, b: ComplexMatrix, x: ComplexMatrix): ComplexMatrix {
    val n = b.rows
    val residual = ComplexMatrix(n, 1)
    for (i in 0 until n) {
        val sum = Complex(0.0, 0.0)
        for (j in 0 until n) {
            sum += A[i, j] * x[j, 0]
        }
        residual[i, 0] = b[i, 0] - sum
    }
    return residual
}
fun complexVectorNorm(vector: ComplexMatrix): Double {
    if(vector.cols != 1) throw Exception("В complexVectorNorm попала Строка ")
    var sum = 0.0
    for (i in vector.data[0].indices) {
        val modulus = vector[i,0].abs2()
        sum += modulus
    }
    return sqrt(sum)
}
private infix fun Double.c(d: Double): Complex = Complex(this,d)
