package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.uiPlots.BuildingDistrict
import modules.uiPlots.DrawData
import modules.uiPlots.SceneDistrict

/** Campus is an abstract class that has a BuildingDistrict attached to it, this can be used as a base for widgets
 * campus is abstract by design and it should be implemented by case, but it can also be inherited to build preset combinations
 * either way it SHOULD not have any add plot or remove plot functions
 * also zooming and screen cropping are not implemented, this is a widget, if you need a widget that you can zoom or crop, what you really need is a dictionary
 * or you can inherit this to make a version that does implement those, your 'fun'eral
 */
abstract class Campus: Building {
    var district =  BuildingDistrict()

    override fun update() {
    }

    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        district.draw(batch,alpha)
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        district.update(hovering)
    }

    override fun updatePunRectangle(r: Rectangle) {
        district.punRectangle = r
    }

}