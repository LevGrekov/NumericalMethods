package math.complex

import kotlin.math.pow

fun QRMethod(
    A: SqComplexMatrix,
    eigens: ArrayList<Complex>,
    maxIterations: Int = 1000
): ArrayList<Complex> {

    val n = A.size
    var resMat = A.copy()
    var iter = 0

    var swish: SqComplexMatrix

    do {
        swish = A.E * resMat[n-1,n-1]
        resMat -= swish
        val R = resMat.copy()
        var Q = A.E

        for (i in 0 until n - 1) {
            val z = ComplexMatrix(n)
            z[i,0] = Complex(1.0)
            val y = ComplexMatrix(n)
            for (j in i until n) {
                y[j,0] = R[j,i]
            }
            var w = y - z * y.complexVectorNorm2()
            w *= (1.0 / w.complexVectorNorm2())

            val Q_i = A.E - w * w.T * 2.0

            val R_i = SqComplexMatrix(Q_i) * R

            for (j in i until n) {
                for (k in i until n) {
                    R[j,k] = R_i[j,k]
                }
            }
            Q *= SqComplexMatrix(Q_i)
        }

        resMat = R * Q
        resMat += swish
        iter ++

    } while(resMat[n-1,n-2].abs() > 10.0.pow(-8.0) && iter < maxIterations)

    eigens.add(resMat[n-1,n-1])

    if(n > 2) {
        val lowA = SqComplexMatrix(n - 1)
        for (i in 0 until n-1) {
            for (j in 0 until n-1) {
                lowA[i,j] = resMat[i,j]
            }
        }
        QRMethod(lowA, eigens)
    } else {
        eigens.add(resMat[0,0])
    }

    return eigens
}