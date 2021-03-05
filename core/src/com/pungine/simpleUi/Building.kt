package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.uiPlots.DrawData


/** Building is the fundamental ui element block for district system, any element should be inherited from it and district calls only the abstract methods
 *
 */
interface Building {
    fun update() //regular update is called after hover function
    fun draw(batch: SpriteBatch,drawData: DrawData, alpha: Float = 1f)
    fun hoverFunction(hovering: Boolean, relativePoint: Point?=null) //this is the function that plots use to report hoveredness, it does not contain checks for it
    fun updatePunRectangle(r: Rectangle){} //this updates the pun rectangle, as default it is an open function and it can be updated per need
}