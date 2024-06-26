package math.complex

import math.eq
import kotlin.math.*

class Complex(var re: Double = 0.0, var im: Double = 0.0)  {
    fun abs() = sqrt(re * re + im * im)
    fun abs2() = re * re + im * im
    fun arg(): Double {
        if (re == 0.0 && im == 0.0) return Double.NaN
        return atan2(im, re)
    }

    operator fun plus(other: Complex) : Complex = Complex(re + other.re, im + other.im)
    operator fun plusAssign(other: Complex){
        re += other.re
        im += other.im
    }



    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)
    operator fun minusAssign(other: Complex): Unit{
        re -= other.re
        im -= other.im
    }

    operator fun times(other: Complex) = Complex(re * other.re - im * other.im, re * other.im + im * other.re)
    operator fun times(other: Double) = Complex( re * other, im * other)
    operator fun div(divisor: Double): Complex {
        return Complex(re / divisor, im / divisor)
    }
    operator fun timesAssign(other: Complex){
        val r = re * other.re - im * other.im
        im = re * other.im + im * other.re
        re = r
    }

    operator fun unaryMinus() = Complex(-re, -im)

    operator fun div(other: Complex) = Complex((re * other.re + im * other.im) / other.abs2(), (im * other.re - re * other.im) / other.abs2())
    operator fun divAssign(other: Complex){
        val r = (re * other.re + im * other.im) / other.abs2()
        im = (im * other.re - re * other.im) / other.abs2()
        re = r
    }

    override fun toString() = buildString {
        if ((re != 0.0) || (im == 0.0)) append("%.10f".format(re))
        if(im != 0.0) {
            append(if(im < 0.0) "-" else if(re != 0.0) "+" else "")
            val formattedIm = if(im.absoluteValue != 1.0) "%.10f".format(im.absoluteValue) else ""
            append(formattedIm)
            append("i")
        }
    }
    private fun conj() = Complex(re, -im)
    fun sin(): Complex {
        return Complex(sin(re) * cosh(im), cos(re) * sinh(im))
    }

    fun cos(): Complex {
        return Complex(cos(re) * cosh(im), -sin(re) * sinh(im))
    }

    operator fun not() = conj()

    fun sign(): Complex = this/this.abs()

    fun sqrt() = Complex(this.abs() * cos(this.arg()) )
    fun toDouble(): Double? = if(im eq 0.0 ) re else null

    infix fun eq(other: Complex) =
        abs(re - other.re) < max(re.ulp, other.re.ulp) * 10.0 &&
        abs(im - other.im) < max(im.ulp, other.im.ulp) * 10.0
    infix fun neq(other: Complex) = !this.eq(other)

    fun copy() = Complex(re, im)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Complex) return false

        if (re != other.re || im != other.im) return false

        return true
    }

    // Переопределение hashCode для корректной работы с коллекциями
    override fun hashCode() = re.hashCode() * 31 + im.hashCode() * 101

}
