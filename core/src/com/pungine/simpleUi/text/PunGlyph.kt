package modules.simpleUi.text

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle


class PunGlyph: GlyphLayout {
    constructor(): super()
    constructor(g: PunGlyph): super(){
        this.font = g.font
        this.text = g.text
        this.textAlignment = g.textAlignment
    }
    constructor(font: BitmapFont, text: String = "Pungine is the best engine in the world, Pungine is the best engine in the world, Pungine is the best engine in the world",textAlignment: TextAlignment = TextAlignment.CENTRE): super(font,text) {
        this.textAlignment=textAlignment
        this.font = font
        this.text = text

    }

    lateinit var font: BitmapFont
    var text = "Pungine is the best engine in the world"
    var textAlignment = TextAlignment.CENTRE

    /** This is a pixel value input, for finding the appropriate glyph
     */
    fun targetHeight(width: Float): Float {
        this.width = width
        setText(font,text, Color.WHITE, width, textAlignment.getHAlign(),true)
        return height
    }

    fun draw(batch: SpriteBatch, rectangle: Rectangle, intendedWidth: Float,alpha: Float){
        width = intendedWidth
        val recordText = text
        font.data.setScale(1f)
        text = recordText
        setText(font,text, Color.WHITE, width, textAlignment.getHAlign(),true)
        val scaling = rectangle.width/intendedWidth
        font.data.setScale(scaling,scaling)
        val y = getTopY(rectangle,intendedWidth)

        val tempColour = font.color
        font.color = Color(tempColour.r,tempColour.g,tempColour.b,alpha)
        font.draw(batch, text, rectangle.left, y, rectangle.width, textAlignment.getHAlign(), true)
        font.color=tempColour

    }



    fun getTopY(rectangle: Rectangle, intendedWidth: Float): Float{
        val scaling = rectangle.width/intendedWidth
        return when (textAlignment.getVAlign()) {
            1 -> rectangle.top - font.data.ascent
            -1 -> rectangle.bottom + (height*scaling+font.data.ascent*0 - font.data.descent)
            else -> rectangle.centre.y + (height*scaling+font.data.ascent*0 - font.data.descent)/2
        }
    }

    fun copy(font: BitmapFont? = null,text: String?=null,textAlignment: TextAlignment?=null): PunGlyph {
        return PunGlyph(font?:this.font, text?: this.text, textAlignment?:this.textAlignment)
    }

    enum class TextAlignment{
        TOP_LEFT {
            override fun getHAlign(): Int { return -1 }
            override fun getVAlign(): Int { return 1 }
        },
        TOP_CENTRE {
            override fun getHAlign(): Int { return 1 }
            override fun getVAlign(): Int { return 1 }
        },
        TOP_RIGHT {
            override fun getHAlign(): Int { return 0 }
            override fun getVAlign(): Int { return 1 }
        },
        LEFT {
            override fun getHAlign(): Int { return -1 }
            override fun getVAlign(): Int { return 0 }
        },
        CENTRE {
            override fun getHAlign(): Int { return 1 }
            override fun getVAlign(): Int { return 0 }
        },
        RIGHT {
            override fun getHAlign(): Int { return 0 }
            override fun getVAlign(): Int { return 0 }
        },
        BOTTOM_LEFT {
            override fun getHAlign(): Int { return -1 }
            override fun getVAlign(): Int { return -1 }
        },
        BOTTOM_CENTRE {
            override fun getHAlign(): Int { return 1 }
            override fun getVAlign(): Int { return -1 }
        },
        BOTTOM_RIGHT {
            override fun getHAlign(): Int { return 0 }
            override fun getVAlign(): Int { return -1 }
        };

        abstract fun getHAlign(): Int
        abstract fun getVAlign(): Int
    }

}


/*
    fun getLastX(rectangle: Rectangle,intendedWidth: Float, lastLine: String): Float{
        text = lastLine
        var initHeight = targetHeight(intendedWidth)
        var lineWidth = intendedWidth
        if(lastLine.length!=0){
            for (i in 1 until intendedWidth.toInt()){
                val th = targetHeight(intendedWidth-i)
                if(th>initHeight){
                    break
                }else{
                    lineWidth = intendedWidth-i
                }
            }
        }else{
            lineWidth=0f
        }

        return when(textAlignment.getHAlign()){
            -1->{ //left
                rectangle.left+lineWidth
            }
            0->{ //right
                rectangle.right
            }
            1 ->{ //centre
                rectangle.centre.x+lineWidth/2
            }else->{
                0f
            }
        }
    }

    fun getLastLine(recordText: String, intendedWidth: Float): String {
        var initialIndex = 0
        var initialHeight = 0f
        for (i in 1 until recordText.length){
            text = recordText.subSequence(0,i).toString()
            val h= targetHeight(intendedWidth)
            if(h>initialHeight){
                initialIndex = i-1
                initialHeight = h
            }
        }
        return recordText.subSequence(initialIndex,recordText.lastIndex).toString()
    }

     */