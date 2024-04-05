package testsolvers

import math.complex.ComplexMatrix
import math.complex.Complex
import math.complex.SqComplexMatrix
import math.slaumethods.SLAU
import math.slaumethods.SquareRootMethodSLAU
import kotlin.math.pow
import kotlin.random.Random


object FourthTestSolver {


    fun solveFirst(first: SLAU){
        println("1 Метод Квадратных Корней")
        println(first.solveSquareRootsMethod())
    }

    fun solveSecond(second:SLAU){
        println("\n2 Метод Простых Итераций")
        println(second.simpleIterations())
    }

    fun solveThird(third:SLAU,C:SqComplexMatrix,tau:Double){
        println("\n3 Метод Зейделя")
        println(third.solveSeidel(tau,C))
    }

    fun solveFourth(fourth:SLAU,C:SqComplexMatrix,tau:Double){
        println("\n4.1 Метод Простых Итераций")
        println(fourth.simpleIterations(tau,C))
    }

    fun solveFifth(fourth:SLAU){
        println("\n4.2 Метод Зейделя 2")
        println(fourth.solveSeidel2())
    }

    fun solveSixth(sixth:SLAU,m:Double,M:Double){
        println("\n4.3 Градиентный Спуск\n")
        sixth.gradientDescent(m,M)?.let {
            println(it)
            println("AbsError: ${SLAU.findAbsError(sixth.gaussianEliminationPartialPivoting(),it)}")
        }
    }


    fun hellishSelectionAlgorithm(A: SqComplexMatrix,randomAbsRange:Double):Pair<SqComplexMatrix,Double>{
        val C = SqComplexMatrix(A.size)
        var tau = 1.0
        while ((A.E - (C * tau) * A).norm() >= 1){
            println((A.E - (C * tau) * A).norm())
            C.fillRandomDouble(-randomAbsRange,randomAbsRange)
            tau = Random.nextDouble(-randomAbsRange,randomAbsRange)
        }
        return Pair(C,tau)
    }

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

    fun hellishSelectionAlgorithm3(A: SqComplexMatrix, b: ComplexMatrix): Pair<SqComplexMatrix,ComplexMatrix> {
        val Anew = SqComplexMatrix(A)
        val bnew = ComplexMatrix(b)
        for (i in 0 until A.rows) {
            var sum = 0.0
            var isDiagonalDominant = false
            while (!isDiagonalDominant){
                for (j in 0 until A.cols) {
                    if (j != i) {
                        sum += A[i, j].abs()
                    }
                }
                println()
                if (A[i, i].abs() > sum) {
                    isDiagonalDominant = true
                }
                else {
                    sum = 0.0
                    var randRowIndex = Int.MIN_VALUE
                    var min = 100000.0
                    for (j in 0 until A.cols){
                        println("${A[j,i]} $min")
                        if (A[j,i].abs() < min && j != i ) {
                            min = A[j,i].abs()
                            randRowIndex = j
                        }
                    }
                    println(randRowIndex)
                    println(A[randRowIndex,i])

                    if (randRowIndex != i) {
                        for (j in 0 until A.cols) {
                            Anew[i, j] = Anew[i, j] + A[randRowIndex, j] // Складываем строку с выбранной случайной строкой
                            bnew[0, j] = bnew[0, j] + b[0, j] // Обновляем соответствующий вектор b
                        }

                        println(Anew)
                        println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
                        Thread.sleep(5000)
                    }
                }
            }
        }
        return Pair(Anew,bnew)
    }

    fun hellishSelectionAlgorithm4(A: SqComplexMatrix, b: ComplexMatrix): Pair<SqComplexMatrix,ComplexMatrix> {
        val Anew = SqComplexMatrix(A)
        val bnew = ComplexMatrix(b)
        for (i in 0 until A.rows) {
            var isDiagonalDominant = false
            while (!isDiagonalDominant){
                var sum = 0.0
                for (j in 0 until A.cols) {
                    if (j != i) {
                        sum += A[i, j].abs()
                    }
                }
                if (A[i, i].abs() > sum) {
                    isDiagonalDominant = true
                }
                else{
                    var index = 0
                    var measure = Pair(-Double.MAX_VALUE,false)
                    for (j in 0 until A.cols){
                        if(i!=j){
                            val candidateMeasure = selectionHelper(i,Anew.data[i],Anew.data[j])
                            if(candidateMeasure.first > measure.first){
                                measure = Pair(candidateMeasure.first,true)
                                index = j
                            }
                            if(candidateMeasure.second > measure.first){
                                measure = Pair(candidateMeasure.first,false)
                                index = j
                            }
                        }
                    }
                    for (j in 0 until A.cols) {
                        if (measure.second){
                            Anew[i, j] = Anew[i, j] + A[index, j] // Складываем строку с выбранной случайной строкой
                            bnew[0, j] = bnew[0, j] + b[0, j] // Обновляем соответствующий вектор b
                        }
                        else{
                            Anew[i, j] = Anew[i, j] - A[index, j] // Складываем строку с выбранной случайной строкой
                            bnew[0, j] = bnew[0, j] - b[0, j] // Обновляем соответствующий вектор b
                        }
                    }
                    println(Anew)
                    println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
                    Thread.sleep(2000)
                }
                sum = 0.0
            }
        }
        return Pair(Anew,bnew)
    }

    fun selectionHelper(i: Int, row: Array<Complex>, candidateRow: Array<Complex>): Pair<Double,Double> {
        println(i)
        val resultRow = row.clone()
        val resultRowM = row.clone()
        val sum = row.filterIndexed { index, _ -> index != i }.sumOf { it.abs() }
        for (j in row.indices) {
            resultRow[j].plusAssign(candidateRow[j])
            resultRowM[j].minusAssign(candidateRow[j])
        }
        val newSumRelation =  resultRow.filterIndexed { index, _ -> index != i }.sumOf { it.abs() } / sum
        val newSumMRelation = resultRowM.filterIndexed { index, _ -> index != i }.sumOf { it.abs() } / sum

        val plus = if (resultRow[i].abs()-newSumRelation < row[i].abs()-sum ) -Double.MAX_VALUE else (resultRow[i].abs()-newSumRelation)
        val minus = if ((resultRowM[i].abs()-newSumMRelation) < (row[i].abs()-sum)) -Double.MAX_VALUE else resultRowM[i].abs()-newSumMRelation
        return Pair(plus,minus)
    }



//    fun firstEx(matrix: SqComplexMatrix, constants: ComplexMatrix) {
//        val a = SquareRootMethodSLAU(matrix,constants)
//        println(a.solve())
//    }
//
//
//    fun thirdEx(matrixA: SqComplexMatrix, constants: ComplexMatrix) {
//        val a = IterationsMethod()
//        val tau = 1.0
//        val C = SqComplexMatrix(
//            arrayOf(
//                arrayOf(Complex(),Complex(0.1), Complex(0.1)),
//                arrayOf(Complex(0.1), Complex(-0.1), Complex(0.0)),
//                arrayOf(Complex(0.2), Complex(-0.2),Complex(0.2))
//            )
//        )
//        val newVars = a.transformToIterativeForm(matrixA,constants,tau,C)
//        println(a.solveSeidel(SqComplexMatrix(newVars.first), ComplexMatrix(newVars.second.data).T))
//    }
//
//    fun fourth(A: SqComplexMatrix, b: ComplexMatrix) {
//        val a = IterationsMethod()
//        val tau = -4.1321123
//        val C = SqComplexMatrix(
//            arrayOf(
//                arrayOf(Complex(0.1),Complex(0.6), Complex(0.3),Complex(0.2)),
//                arrayOf(Complex(0.4), Complex(-1.5), Complex(-0.8),Complex(-0.1)),
//                arrayOf(Complex(-0.3), Complex(-3.1),Complex(-1.0),Complex(0.0)),
//                arrayOf(Complex(-1.3), Complex(-15.6),Complex(-7.8),Complex(-0.3)),
//            )
//        )
//        val C2 = SqComplexMatrix.identity(4)
//        val newVars = a.transformToIterativeForm(A,b,tau, C2)
//        println(a.solveSeidel(SqComplexMatrix(newVars.first), ComplexMatrix(newVars.second.data).T))
//    }



}