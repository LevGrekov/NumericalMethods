import math.complex.Complex
import math.complex.ComplexMatrix
import math.complex.SqComplexMatrix
import math.slaumethods.SLAU
import utils.FileWriter
import kotlin.random.Random

fun hellishSelectionAlgorithm2(A: SqComplexMatrix,randomAbsRange:Double):Pair<SqComplexMatrix,Double>{
    var C = SqComplexMatrix(A.size)
    var tau = 1.0
    while ((A.E - (C * tau) * A).norm() >= 1){
        C.fillRandomDouble(-randomAbsRange,randomAbsRange)
        C += A.Inv
        tau = Random.nextDouble(-randomAbsRange,randomAbsRange)
    }
    return Pair(C,tau)
}
fun hellishSelectionAlgorithm(A: SqComplexMatrix,randomAbsRange:Double):SqComplexMatrix{

    var Cz = SqComplexMatrix(A.size)
    var tau = 1.0
    do{
        Cz.fillRandomDouble(-randomAbsRange,randomAbsRange)
        tau = Random.nextDouble(-randomAbsRange,randomAbsRange)
        Cz += A.Inv * tau
        val B = SLAU.copyElementsFromMatrix(Cz*A) { i, j -> i >= j }
        val C = SLAU.copyElementsFromMatrix(Cz*A) { i, j -> i < j }
    } while ((-B.Inv*C).norm() >= 1 || (Cz*A).determinant eq Complex())
    return Cz
}

fun var12(){
    val first = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(1.35), Complex(-0.72), Complex(1.38)),
                arrayOf(Complex(-0.72), Complex(1.45), Complex(-2.18)),
                arrayOf(Complex(1.38), Complex(-2.18), Complex(0.93))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(0.88), Complex(1.72), Complex(-0.72)))).T
    )
    val second = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.05), Complex(-0.06), Complex(-0.12), Complex(0.14)),
                arrayOf(Complex(0.04), Complex(-0.12), Complex(0.08), Complex(0.11)),
                arrayOf(Complex(0.34), Complex(0.08), Complex(-0.06), Complex(0.14)),
                arrayOf(Complex(0.11), Complex(0.12), Complex(), Complex(-0.03))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(-2.17), Complex(1.4), Complex(-2.1), Complex(-0.8)))).T
    )
    val third = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(4.5), Complex(-3.5), Complex(7.4)),
                arrayOf(Complex(3.1), Complex(-0.6), Complex(-2.3)),
                arrayOf(Complex(0.8), Complex(7.4), Complex(-0.5)),
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(2.5), Complex(-1.5), Complex(6.4)))).T
    )
    val fourth = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.11), Complex(-0.17), Complex(0.72), Complex(-0.34)),
                arrayOf(Complex(0.81), Complex(0.12), Complex(-0.91), Complex(0.17)),
                arrayOf(Complex(0.17), Complex(-0.18), Complex(1.00), Complex(0.23)),
                arrayOf(Complex(0.13), Complex(0.17), Complex(-0.99), Complex(0.35))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(0.17), Complex(1.0), Complex(0.21), Complex(2.71)))).T,
        tolerance = 1e-4
    )
    solveVar(first,second,third,fourth)

}

fun var20(){
    val first = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(1.48), Complex(0.75), Complex(-1.23)),
                arrayOf(Complex(0.75), Complex(0.96), Complex(1.64)),
                arrayOf(Complex(-1.23), Complex(1.64), Complex(-0.55))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(0.83), Complex(-1.12), Complex(0.47)))).T
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
        ComplexMatrix(arrayOf(arrayOf(Complex(-1.2), Complex(0.81), Complex(-0.92), Complex(0.17)))).T
    )

    val third = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(3.8), Complex(6.7), Complex(-1.2)),
                arrayOf(Complex(6.4), Complex(1.3), Complex(-2.7)),
                arrayOf(Complex(2.4), Complex(-4.5), Complex(3.5)),
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(5.2), Complex(3.8), Complex(-0.6)))).T
    )

    val fourth = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(0.11), Complex(-0.17), Complex(0.72), Complex(-0.34)),
                arrayOf(Complex(0.81), Complex(0.12), Complex(-0.91), Complex(0.17)),
                arrayOf(Complex(0.17), Complex(-0.18), Complex(1.00), Complex(0.23)),
                arrayOf(Complex(0.13), Complex(0.17), Complex(-0.99), Complex(0.35))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(0.17), Complex(1.00), Complex(0.21), Complex(2.71)))).T,
        tolerance = 1e-4
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
        ComplexMatrix(arrayOf(arrayOf( Complex(0.21),Complex(0.22), Complex(1.0), Complex(0.6318)))).T,
        tolerance = 1e-4
    )
}

fun solveVar(first:SLAU,second:SLAU,third:SLAU,fourth:SLAU){
    FileWriter.also {
        it.clear()
        it.write("1) Метод Квадратных Корней")
        it.write(first.solveSquareRootsMethod())
        it.separate()

        it.write("2) Метод Простых Итераций")
        it.write(second.simpleIterations())
        it.separate()

        it.write("3) Метод Зейделя")
        val (C,tau) = hellishSelectionAlgorithm2(third._matrix,1.0).also{
            println("Seidel")
            println(it.first)
            println(it.second)
        }
        it.write(third.solveSeidel(tau,C))
        it.separate()

        val gaussSolution = fourth.gaussianEliminationPartialPivoting()

        it.write("4.1) Метод Простых Итераций")
        val (C2,tau2) = hellishSelectionAlgorithm2(fourth._matrix,1.0).also{
            println("SIMPLEITER4.1")
            println(it.first)
            println(it.second)
        }
        val solution41 = fourth.simpleIterations(tau2,C2)
        it.write(solution41)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution41)}")
        it.write("residual: ${fourth.findResidual(solution41)}")
        it.separate()

        it.write("4.2) Метод Зейделя II")
        val Cz = hellishSelectionAlgorithm(fourth._matrix,0.05).also{
            println("SEIDEL II")
            println(it)
        }
        val solution42 = fourth.solveSeidel2(Cz)
        it.write(solution42)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution42)}")
        it.write("residual: ${fourth.findResidual(solution42)}")
        it.separate()

        it.write("4.3) Градиентный Спуск")
        val solution43 = fourth.gradientDescent()
       //val solution43 = fourth.gradientDescent(0.00313913, 50.6563)
        it.write(solution43)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution43)}")
        it.write("residual: ${fourth.findResidual(solution43)}")
        it.separate()

        it.write("4.5) Метод Гаусса с выбором ведущего элемента")
        it.write("$gaussSolution")
    }
}

fun var13(){
    val first = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(1.48), Complex(0.75), Complex(-1.23)),
                arrayOf(Complex(0.75), Complex(0.96), Complex(1.64)),
                arrayOf(Complex(-1.23), Complex(1.64), Complex(-0.55))
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(0.83), Complex(-1.12), Complex(0.47)))).T
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
        ComplexMatrix(arrayOf(arrayOf(Complex(-1.2), Complex(0.81), Complex(-0.92), Complex(0.17)))).T
    )

    val third = SLAU(
        SqComplexMatrix(
            arrayOf(
                arrayOf(Complex(3.8), Complex(6.7), Complex(-1.2)),
                arrayOf(Complex(6.4), Complex(1.3), Complex(-2.7)),
                arrayOf(Complex(2.4), Complex(-4.5), Complex(3.5)),
            )
        ),
        ComplexMatrix(arrayOf(arrayOf(Complex(5.2), Complex(3.8), Complex(-0.6)))).T
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
        ComplexMatrix(arrayOf(arrayOf(Complex(0.22), Complex(0.11), Complex(1.0), Complex(0.21)))).T,
        tolerance = 1e-4
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
        ComplexMatrix(arrayOf(arrayOf( Complex(0.21),Complex(0.22), Complex(1.0), Complex(0.6318)))).T,
        tolerance = 1e-4
    )

    FileWriter.also {
        it.clear()
        it.write("1) Метод Квадратных Корней")
        it.write(first.solveSquareRootsMethod())
        it.separate()

        it.write("2) Метод Простых Итераций")
        it.write(second.simpleIterations())
        it.separate()

        it.write("3) Метод Зейделя")
        val (C,tau) = hellishSelectionAlgorithm2(third._matrix,1.0)
        it.write(third.solveSeidel(tau,C))
        it.separate()

        val gaussSolution = fourth.gaussianEliminationPartialPivoting()

        it.write("4.1) Метод Простых Итераций")
        val (C2,tau2) = hellishSelectionAlgorithm2(fourth._matrix,1.0)
        val solution41 = fourth.simpleIterations(tau2,C2)
        it.write(solution41)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution41)}")
        it.write("residual: ${fourth.findResidual(solution41)}")
        it.separate()

        it.write("4.2) Метод Зейделя II")
        val Cz = hellishSelectionAlgorithm(fourth._matrix,0.05)
        val solution42 = fourth.solveSeidel2(Cz)
        it.write(solution42)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution42)}")
        it.write("residual: ${fourth.findResidual(solution42)}")
        it.separate()

        it.write("4.3) Градиентный Спуск")
        //0.00313913, 50.6563
        val solution43 = fourth.gradientDescent()
        it.write(solution43)
        it.write("AbsError: ${SLAU.findAbsError(gaussSolution,solution43)}")
        it.write("residual: ${fourth.findResidual(solution43)}")
        it.separate()

        it.write("4.5) Метод Гаусса с выбором ведущего элемента")
        it.write("$gaussSolution")


    }
}

fun main(){

    var12()
}
