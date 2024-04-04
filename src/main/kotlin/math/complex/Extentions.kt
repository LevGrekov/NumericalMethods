package math.complex

val Double.j: Complex
    get() = Complex(0.0, this)

operator fun Double.plus(c: Complex): Complex{
    return Complex(this + c.re, c.im)
}

operator fun Double.minus(c: Complex): Complex{
    return Complex(this + c.re, -c.im)
}