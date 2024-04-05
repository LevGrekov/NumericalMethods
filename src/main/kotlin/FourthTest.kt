import math.complex.Complex
import math.complex.SqComplexMatrix
import math.slaumethods.SLAU
import testsolvers.FourthTestSolver


fun main(){
    val first = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(1.48), Complex(0.75), Complex(-1.23)),
                arrayOf(Complex(0.75), Complex(0.96), Complex(1.64)),
                arrayOf(Complex(-1.23), Complex(1.64), Complex(-0.55))
            )
        ),
        arrayOf(
            Complex(0.83), Complex(-1.12), Complex(0.47)
        )
    )
    val second = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.08), Complex(-0.03), Complex(0.0), Complex(-0.04)),
                arrayOf(Complex(0.0), Complex(0.31), Complex(0.27), Complex(-0.08)),
                arrayOf(Complex(0.33), Complex(0.0), Complex(-0.07), Complex(0.21)),
                arrayOf(Complex(0.11), Complex(0.0), Complex(0.03), Complex(0.58))
            )
        ),
        arrayOf(Complex(-1.2), Complex(0.81), Complex(-0.92), Complex(0.17))
    )
    val tau = 1.0
    val C = SqComplexMatrix(
        arrayOf(
            arrayOf(Complex(),Complex(0.1), Complex(0.1)),
            arrayOf(Complex(0.1), Complex(-0.1), Complex(0.0)),
            arrayOf(Complex(0.2), Complex(-0.2),Complex(0.2))
        )
    )
    val third = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(3.8), Complex(6.7), Complex(-1.2)),
                arrayOf(Complex(6.4), Complex(1.3), Complex(-2.7)),
                arrayOf(Complex(2.4), Complex(-4.5), Complex(3.5)),
            )
        ),
        arrayOf(Complex(5.2), Complex(3.8), Complex(-0.6))
    )

    val fourth = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.18), Complex(2.11), Complex(0.13), Complex(-0.22)),
                arrayOf(Complex(0.33), Complex(-0.22), Complex(-1.00), Complex(0.17)),
                arrayOf(Complex(-1.0), Complex(0.11), Complex(2.00), Complex(-0.45)),
                arrayOf(Complex(7.00), Complex(-0.17), Complex(-0.22), Complex(0.33))
            )
        ),
        arrayOf(Complex(0.22), Complex(0.11), Complex(1.0), Complex(0.21))
    )

    val fourthEq = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(7.00), Complex(-0.17), Complex(-0.22), Complex(0.33)),
                arrayOf(Complex(0.18), Complex(2.11), Complex(0.13), Complex(-0.22)),
                arrayOf(Complex(-1.0), Complex(0.11), Complex(2.00), Complex(-0.45)),
                arrayOf(Complex(-0.015600000000000003), Complex(3.999999999999837E-4), Complex(0.006000000000000005), Complex(-0.066)),
            )
        ),
        arrayOf( Complex(0.21),Complex(0.22), Complex(1.0), Complex(0.6318),)
    )


    FourthTestSolver.apply {
        solveFirst(first)
        solveSecond(second)
        val (C,tau) = hellishSelectionAlgorithm2(third.matrix,1.0)
        solveThird(third,C,tau).also {
            println(C)
            println(tau)
        }
        val (C2,tau2) = hellishSelectionAlgorithm2(fourth.matrix,1.0)
        solveFourth(fourth,C2,tau2).also {
            println(C2)
            println(tau2)
        }

        solveFifth(fourthEq).also {
            println(C2)
            println(tau2)
        }
        solveSixth(fourth,0.00313913, 50.6563)
    }
}
