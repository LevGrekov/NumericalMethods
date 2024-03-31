package math.complex

val Double.j: ComplexNum
    get() = ComplexNum(0.0, this)

operator fun Double.plus(c: ComplexNum): ComplexNum{
    return ComplexNum(this + c.re, c.im)
}

operator fun Double.minus(c: ComplexNum): ComplexNum{
    return ComplexNum(this + c.re, -c.im)
}