package modules.visuals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object FontGenerator {
    val usedFonts = mutableListOf<Triple<String, Int, BitmapFont>>()
    var frequentPuntoList = mutableListOf(8, 10, 12, 16, 20, 24, 28, 32, 36, 40, 48, 56, 64, 72, 80, 96, 112)
        set(value) {
            field = value.sorted().toMutableList()
        }

    /** This function, takes a range as upper and lower bounds, with a text and returns a list of puntos that fits inbetween
     */
    fun getFontsBetween(s: IntRange, fontPath: String): List<BitmapFont> {
        return frequentPuntoList.filter { it in s }.map { getFont(fontPath, it) }
    }

    /** Looks for a fontPath and size pair at cache, if it's not found, creates a new one
     */
    private fun createNewFont(size: Int, fontPath: String): BitmapFont {
        val ftfg = FreeTypeFontGenerator(Gdx.files.internal(fontPath))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.color = Color.WHITE
        parameter.size = size
        return ftfg.generateFont(parameter).also {
            ftfg.dispose()
        }
    }

    fun getFont(fontPath: String, size: Int): BitmapFont {
        val size = frequentPuntoList.first { it >= size }
        var ind = usedFonts.firstOrNull { it.first == fontPath && it.second == size }
        if (ind == null) {
            ind = Triple(fontPath, size, createNewFont(size, fontPath))
            usedFonts.add(ind)
        }
        return ind.third
    }


    fun createFont(fontPath: String, size: Int) {
        val size = frequentPuntoList.first { it >= size }
        if (usedFonts.none { it.first == fontPath && it.second == size }) {
            usedFonts.add(Triple(fontPath, size, createNewFont(size, fontPath)))
        }
    }

    fun dispose() {
        usedFonts.forEach {
            try {
                it.third.dispose()
            }catch (e: Exception){

            }

        }
    }
}