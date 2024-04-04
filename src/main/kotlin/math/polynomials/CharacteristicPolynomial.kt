package math.polynomials

import math.complex.Complex
import math.complex.SqComplexMatrix

class CharacteristicPolynomial {


    val matrixC = SqComplexMatrix(
        arrayOf(
            arrayOf(Complex(1.0),Complex(2.0),Complex(4.0)),
            arrayOf(Complex(9.0), Complex(20.0), Complex(5.0)),
            arrayOf(Complex(8.0), Complex(7.0), Complex(6.0))
        )
    )

    // Получаем размерность матрицы
    val n = matrixC.size

    // Создаем нулевую матрицу нужного размера для хранения коэффициентов характеристического полинома
    val coefficients: MutableList<Complex> = mutableListOf()

    // Создаем единичную матрицу нужного размера
    val identity = matrixC.sizE

    fun solve(){

        val map: MutableMap<Int,Double> = mutableMapOf()
        for(i in 0..n){
            val lambda = i.toDouble() // lambda принимает значения от 0 до n
            val characteristicMatrix = matrixC - (identity.times(lambda)) // matrix - lambda * identity
            val characteristicDeterminant = SqComplexMatrix(characteristicMatrix).determinant
            map[i] = characteristicDeterminant.re
            coefficients.add(characteristicDeterminant)
        }
        val poly = Polynomial(map).also { println(it) }
    }
}
