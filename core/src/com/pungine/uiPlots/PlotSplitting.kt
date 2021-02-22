package modules.uiPlots

import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Origin
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.uiPlots.Plot

/** This class in particular handles plot splitting
 * It exists
 *
 */
interface PlotSplitting {
    open fun addFullPlot(id: String, r: Rectangle = FastGeometry.unitSquare(), z: Int = 0, takeOriginAs: Origin = Origin.BOTTOMLEFT): Plot {
        Plot(id, r.switchOrigin(Origin.BOTTOMLEFT, takeOriginAs), z).also {
            addToPlots(it)
            return it
        }
    }

    /** This function takes an id and a rectangle as input and creates a slicing of the given rectangle, then adds stuff to the plots
     * id is the name of the plot
     * r is the rectangle that define borders
     * row & col are for the partition
     * retainOriginal adds the unpartitioned plot to the plots
     */
    fun splitToPlots(id: String, r: Rectangle = FastGeometry.unitSquare(), row: Int = 1, col: Int = 1, retainOriginal: Boolean = false, z: Int = 0, takeOriginAs: Origin = Origin.BOTTOMLEFT): MutableList<Plot> {
        val bigPlot = Plot(id, r.switchOrigin(Origin.BOTTOMLEFT, takeOriginAs), z)
        val smallPlots = bigPlot.gridEqual(id, row, col)
        if (retainOriginal) smallPlots.add(0, bigPlot)//plots.add(bigPlot)
        addToPlots(smallPlots)
        return smallPlots
    }


    /** This is an alternate call for the function above
     */
    fun splitToPlots(id: String, r: Rectangle = FastGeometry.unitSquare(), rows: List<Float> = listOf(1f), cols: List<Float> = listOf(1f), retainOriginal: Boolean = false, z: Int = 0, takeOriginAs: Origin = Origin.BOTTOMLEFT): MutableList<Plot> {
        val bigPlot = Plot(id, r.switchOrigin(Origin.BOTTOMLEFT, takeOriginAs), z)
        val smallPlots = bigPlot.gridBiased(id, rows, cols)
        if (retainOriginal) smallPlots.add(0, bigPlot)
        addToPlots(smallPlots)
        return smallPlots
    }


    /** This merges two plots to create a super plot and adds that to plots
     * the bottom left and top right corners of the given ids are used
     */
    fun superPlot(plotId: String, p1: Plot, p2: Plot, z: Int? = null): Plot {
        return p1.merge(plotId, p2, z).also {
            addToPlots(it)
        }
    }

    abstract fun addToPlots(p: Plot)
    abstract fun addToPlots(p: List<Plot>)
}