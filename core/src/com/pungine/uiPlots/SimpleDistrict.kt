package modules.uiPlots

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.uiPlots.UrbanPlanning
import modules.application.PuniversalValues

abstract class SimpleDistrict(): UrbanPlanning() {
    abstract fun getPlayingField(): Rectangle

    override fun draw(batch: SpriteBatch, alpha: Float) {
        plots.sortedBy { it.z }.forEach {
            if(it.visible){
                val punRect = getPlayingField().getSubRectangle(it.estate)
                it.element?.draw(batch, DrawData(punRect,getPlayingField().getSubRectangle(it.estate),it.zoomRectangle,getPlayingField()),alpha)
            }
        }
    }

    override fun update(hoveredOn: Boolean) {
        var holder = false // following handles clicks
        for (i in plots.sortedBy { it.z }.reversed()){ // in reverse z order
            val h = getPlayingField().getSubRectangle(i.estate)
            i.element?.updatePunRectangle(h)
            if(holder||i.inactive||!i.visible||!hoveredOn|| !getPlayingField().contains(PuniversalValues.cursorPoint)){ //if touch was consumed or i is inactive
                i.hovering = false // hovering is false
            }else{ // hover is checked
                i.hovering = h.contains(PuniversalValues.cursorPoint)
                holder = (i.hovering&&i.touchStopper)
            }
        }

        plots.forEach {
            it.update(getPlayingField().getSubRectangle(it.estate).getNormalPoint(PuniversalValues.cursorPoint))
        }
    }


    /** This function returns the normalized version of a point in pun coordinates
     * if unZoom is true then the rated point on the original (unzoomed) estate is returned
     */
    fun getPunRatedPointOnPlot(id: String, p: Point, unZoom: Boolean=false): Point {
        return if(unZoom){
            val thatP = getPlayingField().getSubRectangle( findPlot(id).estate).getNormalPoint(p)
            FastGeometry.unitSquare().invertSubRectangle(findPlot(id).zoomRectangle).getNormalPoint(thatP)
        }else{
            getPlayingField().getSubRectangle( findPlot(id).estate).getNormalPoint(p)
        }
    }
}