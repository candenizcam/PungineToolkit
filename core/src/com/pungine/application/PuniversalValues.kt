package modules.application

import com.badlogic.gdx.Gdx
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle

object PuniversalValues {
    var punWidth = 1280f // pre usage width
    var punHeight = 720f // pre usage height
    val appWidth: Float
        get() {
            return Gdx.graphics.width.toFloat()
        }
    val appHeight: Float
        get() {
            return Gdx.graphics.height.toFloat()
        }
    val frameRectangle: Rectangle
        get() {
            return Rectangle(0f,Gdx.graphics.width.toFloat(),0f,
                Gdx.graphics.height.toFloat()).getSubRectangle(punWidth.toFloat(), punHeight.toFloat())
        }
    val cursorPoint: Point
        get() {
            return Point(Gdx.input.x.toFloat(), appHeight-Gdx.input.y.toFloat())
        }
    val ratedWidth: Float
        get() {
            return frameRectangle.width
        }
    val ratedHeight: Float
        get() {
            return frameRectangle.height
        }
    val appCentre: Point
        get() {
            return Point(appWidth/2f, appHeight/2f)
        }
}