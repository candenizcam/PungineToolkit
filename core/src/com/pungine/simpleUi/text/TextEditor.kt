package modules.simpleUi.text

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.inputProcessor.InputHandler
import modules.uiPlots.DrawData

class TextEditor(fontPath: String, punto:Int,initialText: String="", alignment: PunGlyph.TextAlignment=PunGlyph.TextAlignment.CENTRE, colour: Color = Color.WHITE): TextBox(initialText, fontPath, alignment, punto, punto, colour) {
    var clickCaptured = false
    private var blinkTimeHolder = 0f


    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        if(clickCaptured){
            blinkTimeHolder += Gdx.graphics.deltaTime
            blinkTimeHolder %= 1.5f
        }else{
            blinkTimeHolder=0f
        }
        if(blinkTimeHolder<=0.75f){

            super.draw(batch, drawData,alpha)
        }else{
            super.draw(batch, drawData,alpha*0.7f)
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
            text = text.dropLast(1)
        }else if(t!=""){
            text +=t
        }
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        if(Gdx.input.justTouched()){
            clickCaptured = hovering
        }
    }
}