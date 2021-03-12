package modules.simpleUi.text

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.inputProcessor.InputHandler
import modules.application.PuniversalValues
import modules.basic.Colour
import modules.uiPlots.DrawData

class TextEditor: TextBox{
    constructor(fontPath: String, punto:Int, initialText: String="", alignment: PunGlyph.TextAlignment=PunGlyph.TextAlignment.CENTRE, colour: Colour = Colour.WHITE, digit: Int?=null): super(initialText, fontPath, alignment, punto, punto, colour){
        this.digit = digit
    }
    constructor(textBox: TextBox, digit: Int?=null): super(textBox){
        this.digit = digit
    }

    var digit: Int?= null //sets an upper bound for digit number
    var clickCaptured = false
    private var blinkTimeHolder = 0f
    private var doubleClickTimeHolder = 0f
    var doubleClickTime = PuniversalValues.doubleClickTime
    private var doubleClicked = false


    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        if(clickCaptured){
            blinkTimeHolder += Gdx.graphics.deltaTime
            blinkTimeHolder %= 1.5f
        }else{
            blinkTimeHolder=0f
        }
        if(doubleClicked){
            if(((blinkTimeHolder)*2f)%1<=0.5f){
                super.draw(batch, drawData,alpha)
            }else{
                super.draw(batch, drawData,alpha*0.3f)
            }
        }else{
            if(blinkTimeHolder<=0.75f){
                super.draw(batch, drawData,alpha)
            }else{
                super.draw(batch, drawData,alpha*0.7f)
            }
        }


    }

    override fun update() {
        if(clickCaptured){
            textInputs()
        }
        super.update()
    }

    private fun textInputs(){
        var t = InputHandler.letterJustPressed()
        t += InputHandler.numberJustPressed()


        if(InputHandler.backspacePressed()){
            text = if(doubleClicked){
                doubleClicked = false
                ""
            }else{
                text.dropLast(1)
            }
        }else if(t!=""){
            if(doubleClicked){
                doubleClicked = false
                text = t
            }else{
                if(text.length<digit?:text.length+1){
                    text +=t
                }
            }
        }
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        doubleClickTimeHolder += Gdx.graphics.deltaTime
        if(Gdx.input.justTouched()){
            doubleClicked = (doubleClickTimeHolder<doubleClickTime)
            doubleClickTimeHolder = 0f
            clickCaptured = hovering
        }
    }
}