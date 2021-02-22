package com.pungo.modules.uiPlots

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
//import com.pungo.modules.lcsModule.GetLcsRect
//import com.pungo.modules.lcsModule.LcsVariable
import modules.uiPlots.PlotSplitting

//import com.pungo.modules.uiElements.TextBox
/** This class implements plot splitting methods, and it also contains a list of plots upon which some operations are defined
 *
 */
abstract class UrbanPlanning: PlotSplitting{
    open var plots = mutableListOf<Plot>() // these are references to the plots


    /** This finds plots in specific main rectangle coordinate (between 0 & 1)
     */
    fun findPlot(p: Point): List<Plot> {
        return plots.filter { it.estate.contains(p) }
    }

    /** This finds the plot with the spesific id
     */
    fun findPlot(id: String): Plot {
        try {
            return plots.filter { it.id == id }[0]
        } catch (e: Exception) {
            throw Exception("no id: $id found")
        }
    }

    /** This generates the id by adding row & col values
     *
     */
    fun findPlot(id: String, row: Int, col: Int): Plot {
        try {
            return plots.filter { it.id == "${id}_r${row}_c${col}" }[0]
        } catch (e: Exception) {
            throw Exception("no id ${"${id}_r${row}_c${col}"} found")
        }
    }

    /** movement check returns true if there is movement
     */
    fun movementCheck(): Boolean {
        return !plots.none { it.locations.moving }
    }

    override fun addToPlots(p: Plot) {
        plots.add(p)
        if (plots.size != plots.distinctBy { it.id }.size) throw Exception("id clasht at district")
    }

    override fun addToPlots(p: List<Plot>) {
        plots.addAll(p)
        if (plots.size != plots.distinctBy { it.id }.size) throw Exception("id clasht at district")
    }

    fun movePlot(id: String, dx: Float, dy: Float) {
        findPlot(id).movePlot(dx, dy)
    }


    fun setVisible(id: String, visible: Boolean) {
        plots.first { it.id == id }.visible = visible
    }

    fun makeAllInvisible() {
        plots.forEach {
            it.visible = false
        }
    }

    abstract fun update()
    abstract fun draw(batch: SpriteBatch, alpha: Float = 1f)
}