package com.toolkit

import modules.basic.Colours
import modules.simpleUi.text.ColouredTextBox
import modules.simpleUi.text.PunGlyph
import modules.simpleUi.text.TextBox

object References {
    val appWidth = 1280
    val appHeight = 720
    val bgColour = Colours.byRGBA256(24,21,35)
    val buttonBg = Colours.byRGBA256(34,31,45)


    fun buttonTextBox(text: String, maxPunto: Int=36): ColouredTextBox {
        return ColouredTextBox(text,"font/MPLUSRounded1c-Bold.ttf", PunGlyph.TextAlignment.CENTRE,maxPunto,bgColour = buttonBg)
    }

    fun textBox(text: String, maxPunto: Int=36): TextBox {
        return TextBox(text,"font/MPLUSRounded1c-Bold.ttf", PunGlyph.TextAlignment.CENTRE,maxPunto)
    }

}