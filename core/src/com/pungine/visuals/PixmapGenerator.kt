package com.pungo.modules.visuals

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import modules.application.PuniversalValues
import modules.visuals.PuxMap
import kotlin.math.ceil

/** Used to create custom pixmaps which are returned as CustomPixmap visual elements
 *
 */
object PixmapGenerator {
    fun singleColour(): PuxMap {
        PuxMap(11, 11, Pixmap.Format.RGBA8888).also {
            it.setColor(1f, 1f, 1f, 1f)
            it.fill()
            return it
        }
    }


    /** Creates a grid with col and row
     */
    fun grid(row: Int, col: Int): PuxMap {
        val w = ceil(PuniversalValues.punWidth).toInt()
        val h = ceil(PuniversalValues.punHeight).toInt()
        PuxMap(w, h, Pixmap.Format.RGBA8888).also {
            it.setColor(Color.LIGHT_GRAY) //sets colour permanently
            it.drawRectangle(0, 0, w, h)
            for (i in (1 until col)) {
                val xVal = w * (i.toFloat() / col)
                it.fillRectangle(ceil(xVal).toInt() - 1, 0, 3, h)
            }
            for (i in (1 until row)) {
                val yVal = h * (i.toFloat() / row)
                it.fillRectangle(0, ceil(yVal).toInt() - 1, w, 3)
            }
            return it
        }
    }

    fun circle(): PuxMap {
        PuxMap(101, 101, Pixmap.Format.RGBA8888).also {
            it.setColor(1f, 1f, 1f, 1f)
            it.fillCircle(50, 50, 50)
            return it
        }
    }
}