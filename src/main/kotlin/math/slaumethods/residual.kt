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
fun complexVectorNorm(vector: Array<Complex>): Double {
    var sum = 0.0
    for (i in vector.indices) {
        val modulus = vector[i].abs2()
        sum += modulus
    }
    return sqrt(sum)
}
private infix fun Double.c(d: Double): Complex = Complex(this,d)
