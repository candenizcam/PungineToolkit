package modules.uiPlots

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.uiPlots.UrbanPlanning
import modules.application.PuniversalValues

class SceneDistrict(id: String, private var w: Float, private var h: Float, var resizeReaction: ResizeReaction = ResizeReaction.STRETCH): UrbanPlanning() {
    override fun draw(batch: SpriteBatch, alpha: Float) {
        plots.sortedBy { it.z }.forEach {
            if(it.visible){
                val dr = DrawingRectangle(getPlayingField(),w,h,it.estate)
                if(dr.toBeDrawn()){
                    it.element?.draw(batch,dr)
                }
            }
        }
    }

    override fun update() {
        var holder = false // following handles clicks
        for (i in plots.sortedBy { it.z }.reversed()){ // in reverse z order
            if(holder||i.inactive|| !getPlayingField().contains(PuniversalValues.cursorPoint)){ //if touch was consumed or i is inactive
                i.hovering = false // hovering is false
            }else{ // hover is checked
                val h = getPlayingField().getSubRectangle(i.estate).contains(PuniversalValues.cursorPoint)
                i.hovering = h
                holder = (h&&i.touchStopper)
            }
        }

        plots.forEach {
            it.update(getPlayingField().getSubRectangle(it.estate).getNormalPoint(PuniversalValues.cursorPoint))
        }
    }


    /** This function returns the normalized version of a point in pun coordinates
     */
    fun getPunRatedPointOnPlot(id: String, p: Point): Point {
        return Rectangle(0f,w,0f,h).getSubRectangle( findPlot(id).estate).getNormalPoint(p)
        //return getPlayingField().getSubRectangle( findPlot(id).estate).getNormalPoint(p)
    }




    private fun getPlayingField(): Rectangle {
        return when(resizeReaction){
            ResizeReaction.STATIC-> Rectangle(w,h, PuniversalValues.appCentre)
            ResizeReaction.STRETCH -> Rectangle(PuniversalValues.appWidth,PuniversalValues.appHeight,PuniversalValues.appCentre)
            ResizeReaction.RATED -> Rectangle(PuniversalValues.appWidth,PuniversalValues.appHeight,PuniversalValues.appCentre).getSubRectangle(w,h)
        }
    }

    fun setSizeReference(w: Float, h: Float){
        this.w = w
        this.h = h
    }

    enum class ResizeReaction{
        STATIC, //retains given pixels
        STRETCH, //stretches to the app
        RATED //retains ratio while stretching up to the app
    }
}