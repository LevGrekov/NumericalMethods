package math.complex

class Vector(data: Array<Complex>) : ComplexMatrix(arrayOf(data)) {

    constructor(size: Int) : this(Array(size) { Complex() })

    val size: Int
        get() = rows

    operator fun get(index: Int): Complex {
        return data[0][index]
    }

    operator fun set(index: Int, value: Complex) {
        data[0][index] = value
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in 0 until size) {
            builder.append("${this[i]} ")
        }
        return builder.toString()
    }

    operator fun times(other: Vector) = ComplexMatrix(super.times(other))
    override operator fun times(scalar:Double) = ComplexMatrix(super.times(scalar))
    operator fun plus(other: SqComplexMatrix) = ComplexMatrix(super.plus(other))
    operator fun minus(other: SqComplexMatrix) = ComplexMatrix(super.minus(other))
    override operator fun unaryMinus() = ComplexMatrix(super.unaryMinus())
}