package modules.uiPlots

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.uiPlots.UrbanPlanning
import modules.application.PuniversalValues
import modules.simpleUi.Displayer

class SceneDistrict(id: String, var w: Float, var h: Float, var resizeReaction: ResizeReaction = ResizeReaction.STRETCH): SimpleDistrict() {
    override fun getPlayingField(): Rectangle {
        return when(resizeReaction){
            ResizeReaction.STATIC-> Rectangle(w,h, PuniversalValues.appCentre)
            ResizeReaction.STRETCH -> Rectangle(PuniversalValues.appWidth,PuniversalValues.appHeight,PuniversalValues.appCentre)
            ResizeReaction.RATED -> Rectangle(PuniversalValues.appWidth,PuniversalValues.appHeight,PuniversalValues.appCentre).getSubRectangle(w,h)
        }
    }


    enum class ResizeReaction{
        STATIC, //retains given pixels
        STRETCH, //stretches to the app
        RATED //retains ratio while stretching up to the app
    }
}