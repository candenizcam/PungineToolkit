package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import modules.uiPlots.DrawingRectangle


/** Building is the fundamental ui element block for district system, any element should be inherited from it and district calls only the abstract methods
 *
 */
interface Building {
    fun update()
    fun draw(batch: SpriteBatch, drawingRectangle: DrawingRectangle)
    fun hoverFunction(hovering: Boolean, relativePoint: Point?=null) //this is the function that plots use to report hoveredness, it does not contain checks for it
}