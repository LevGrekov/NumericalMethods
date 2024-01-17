package drawing.convertation

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object Stepic {
    fun getMagnitude(number: Double): Double =
        10.0.pow(floor(log10(abs(number))))

    fun getLowerLim(xMin: Double, step: Double) : Double {
        val ll = step * floor(xMin / step)
        return  if(ll>xMin) ll else ll+step
    }
    fun getStepInnovation(delta: Double,firstEnter: Double = 28.0) : Double{
        val sgn = if((delta/firstEnter).toInt() > 0) 1.0 else -1.0
        var i = 0.0
        while(i!=sgn*100){
            if(delta/firstEnter in (2.0.pow(i - 1.0) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i)))
                return 2.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i) * 5.0.pow(i)) .. (2.0.pow(i+1) * 5.0.pow(i)))
                return 5.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i+1) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i+1)))
                return 10.0 * 10.0.pow(i)
            i+=sgn
        }
        return delta
    }
}
