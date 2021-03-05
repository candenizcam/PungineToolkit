package modules.basic

import com.badlogic.gdx.graphics.Color
import kotlin.math.abs

/** This object creates Color's from various common input types
 */
object Colours{

    /** Returns color declared by int RGB {0,1,...255}
     */
    fun byRGBA256(r: Int, g: Int, b: Int, a: Int = 255): Color {
        return Color((r/255f).coerceIn(0f,1f),(g/255f).coerceIn(0f,1f),(b/255f).coerceIn(0f,1f),(a/255f).coerceIn(0f,1f))
    }

    fun byRGB(r: Float, g: Float, b: Float, a: Float=1f): Color {
        return Color(r.coerceIn(0f,1f),g.coerceIn(0f,1f),b.coerceIn(0f,1f),a.coerceIn(0f,1f))
    }

    /** Returns colour declared by float HSVA as [0f,1f]
     *
     */
    fun byHSV(h: Float, s: Float, v: Float, a: Float=1f): Color {
        val h = h.coerceIn(0f,1f)
        val s = s.coerceIn(0f,1f)
        val v = v.coerceIn(0f,1f)
        val a = a.coerceIn(0f,1f)
        val hCirc = (h*360).toInt()
        val c = v*s
        val m: Float = v-c
        val x: Float = c*(1f - abs((hCirc/60f)%2-1f))
        val rgb = when(hCirc){
            in 0..60 ->{
                Triple(c+m,x+m,m)
            }
            in 60..120->{
                Triple(x+m,c+m,m)
            }
            in 120..180->{
                Triple(m,c+m,x+m)
            }
            in 180..240->{
                Triple(m,x+m,c+m)
            }
            in 240..300->{
                Triple(x+m,m,c+m)
            }
            in 300..360->{
                Triple(c+m,m,x+m)
            }
            else ->{
                Triple(m,m,m)
            }
        }
        return Color(rgb.first,rgb.second,rgb.third,a)
    }

    /** Returns hsv as declared by int {0,1...255}
     */
    fun byHSV256(h: Int, s: Int, v: Int, a: Int=255): Color {
        return byHSV((h/255f).coerceIn(0f,1f),(s/255f).coerceIn(0f,1f),(v/255f).coerceIn(0f,1f),(a/255f).coerceIn(0f,1f))
    }

    /** hex code entry to get colour
     * warning, # is omitted
     */
    fun byHex(hexCode: String): Color {
        return when(hexCode.length){
            3->{
                val r = decodeHex(hexCode[0])*17
                val g = decodeHex(hexCode[1])*17
                val b = decodeHex(hexCode[2])*17
                byRGBA256(r,g,b)
            }
            4->{
                val r = decodeHex(hexCode[0])*17
                val g = decodeHex(hexCode[1])*17
                val b = decodeHex(hexCode[2])*17
                val a = decodeHex(hexCode[3])*17
                byRGBA256(r,g,b,a)
            }
            6->{
                val r = decodeHex(hexCode[0])*16 + decodeHex(hexCode[1])
                val g = decodeHex(hexCode[2])*16 + decodeHex(hexCode[3])
                val b = decodeHex(hexCode[4])*16 + decodeHex(hexCode[5])
                byRGBA256(r,g,b)
            }
            8->{
                val r = decodeHex(hexCode[0])*16 + decodeHex(hexCode[1])
                val g = decodeHex(hexCode[2])*16 + decodeHex(hexCode[3])
                val b = decodeHex(hexCode[4])*16 + decodeHex(hexCode[5])
                val a = decodeHex(hexCode[6])*16 + decodeHex(hexCode[7])
                byRGBA256(r,g,b,a)
            }
            else -> {
                throw Exception("Error, $hexCode is invalid")
            }


        }
    }

    private fun decodeHex(c: Char): Int {
        return try{
            "$c".toInt()
        }catch (e: Exception){
            when(c){
                'A' -> 10
                'B' -> 11
                'C' -> 12
                'D' -> 13
                'E' -> 14
                'F' -> 15
                else -> throw (Exception("Error, invalid char $c for hex decoding"))
            }
        }
    }
}