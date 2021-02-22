package modules.simpleUi

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.basic.Colours
import modules.simpleUi.text.ColouredTextBox
import modules.uiPlots.DrawingRectangle

class SetButton: Building {
    constructor(upDisplayer: DisplayBuilding, downDisplayer: DisplayBuilding?=null, upRectangle: Rectangle=FastGeometry.unitSquare(), downRectangle: Rectangle=FastGeometry.unitSquare()){
        buttonVisuals.add(ButtonVisuals(ButtonId.UP,upDisplayer,upRectangle))
        buttonVisuals.add(ButtonVisuals(ButtonId.DOWN,downDisplayer?:upDisplayer.copy().also {
            val c = upDisplayer.getColour()
            it.recolour(Colours.byRGB(c.r*0.5f,c.g*0.5f,c.b*0.5f))
            if(it is ColouredTextBox){
                val baseColour = it.getBgColour()
                it.bgRecolour(Colours.byRGB(baseColour.r*0.5f,baseColour.g*0.5f,baseColour.b*0.5f))
            }
                                                                                             },downRectangle))

    }

    constructor(visual: DisplayBuilding,  offRatio: Float, inactiveRatio: Float?=null, hoverRatio: Float?=null,rectangle: Rectangle=FastGeometry.unitSquare()){
        val baseColour = visual.getColour()
        buttonVisuals.add(ButtonVisuals(ButtonId.UP,visual,rectangle))
        val offVisual = visual.copy().also {
            it.recolour(Colours.byRGB(baseColour.r*offRatio,baseColour.g*offRatio,baseColour.b*offRatio))
            if(it is ColouredTextBox){
                val baseColour = it.getBgColour()
                it.bgRecolour(Colours.byRGB(baseColour.r*offRatio,baseColour.g*offRatio,baseColour.b*offRatio))
            }
        }
        buttonVisuals.add(ButtonVisuals(ButtonId.DOWN,offVisual,rectangle))

        if(inactiveRatio!=null){
            val inactiveColour = Colours.byRGB(baseColour.r*inactiveRatio,baseColour.g*inactiveRatio,baseColour.b*inactiveRatio)
            buttonVisuals.add(ButtonVisuals(ButtonId.INACTIVE,visual.copy().also {
                it.recolour(inactiveColour)
                if(it is ColouredTextBox){
                    val baseColour = it.getBgColour()
                    it.bgRecolour(Colours.byRGB(baseColour.r*inactiveRatio,baseColour.g*inactiveRatio,baseColour.b*inactiveRatio))
                }
                                                                                 },rectangle))

        }
        if(hoverRatio!=null){
            val hoverColour = Colours.byRGB(baseColour.r*hoverRatio,baseColour.g*hoverRatio,baseColour.b*hoverRatio)
            buttonVisuals.add(ButtonVisuals(ButtonId.HOVER,visual.copy().also {
                it.recolour(hoverColour)
                if(it is ColouredTextBox){
                    val baseColour = it.getBgColour()
                    it.bgRecolour(Colours.byRGB(baseColour.r*hoverRatio,baseColour.g*offRatio,hoverColour.b*hoverRatio))
                }

                                                                              },rectangle))
        }
    }


    var activeVisual = ButtonId.UP
    var clicked = {
        println("heeyheeey")
    }
    var inactive = false

    fun setHovering(displayer: Displayer, rectangle: Rectangle = FastGeometry.unitSquare()){
        buttonVisuals = buttonVisuals.filter { it.id!=ButtonId.HOVER }.toMutableList()
        buttonVisuals.add(ButtonVisuals(ButtonId.HOVER,displayer,rectangle))
    }

    fun setInactive(displayer: Displayer, rectangle: Rectangle = FastGeometry.unitSquare()){
        buttonVisuals = buttonVisuals.filter { it.id!=ButtonId.INACTIVE }.toMutableList()
        buttonVisuals.add(ButtonVisuals(ButtonId.INACTIVE,displayer,rectangle))
    }

    var buttonVisuals = mutableListOf<ButtonVisuals>()

    override fun update() {
        buttonVisuals.forEach { it.visual.update() }
    }


    override fun draw(batch: SpriteBatch, drawingRectangle: DrawingRectangle) {
        var av = buttonVisuals.filter { it.id==activeVisual }
        if(av.isEmpty()){
            av =  buttonVisuals.filter { it.id == ButtonId.UP }
        }
        val dr = drawingRectangle.ratedCopy(av[0].rectangle)
        if(dr.toBeDrawn()){
            av[0].visual.draw(batch, dr)
        }

    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        activeVisual = if(inactive){
            ButtonId.INACTIVE
        }else{
            if(hovering){
                if(Gdx.input.isTouched){
                    if(activeVisual==ButtonId.DOWN||Gdx.input.justTouched()){
                        ButtonId.DOWN
                    } else{
                        activeVisual
                    }
                }else{
                    if(activeVisual==ButtonId.DOWN){
                        clicked()
                    }
                    ButtonId.HOVER
                }
            }else{
                ButtonId.UP
            }
        }
    }

    data class ButtonVisuals(var id: ButtonId, var visual: DisplayBuilding, var rectangle: Rectangle)

    enum class ButtonId{
        UP,
        DOWN,
        INACTIVE,
        HOVER
    }
}