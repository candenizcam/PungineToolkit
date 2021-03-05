package modules.uiPlots

import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle

class BuildingDistrict: SimpleDistrict() {
    var punRectangle = Rectangle(0f,1f,0f,1f)


    fun setSizeReference(w: Float, h: Float, centre: Point?=null){
        punRectangle = Rectangle(w,h,centre?:punRectangle.centre)
    }

    override fun getPlayingField(): Rectangle {
        return punRectangle
    }
}