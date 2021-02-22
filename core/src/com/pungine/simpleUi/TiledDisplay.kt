package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.uiPlots.DrawingRectangle

class TiledDisplay(val cols: Int, val rows: Int) :Building {
    val tiles = mutableListOf<Tile>()
    val tileLocations = mutableListOf<TileLocation>()


    /** This function modifies a spesific point on the grid
     * null id means erase
     */
    fun modifyGrid(id: String?, row: Int, col: Int){
        tileLocations.removeIf { (it.row==row)&&(it.col==col) }
        if(id!=null){
            tileLocations.add(TileLocation(id,row,col))
        }
    }

    fun clearGrid(){
        tileLocations.clear()
    }


    /** adds, tile, null input means erase
     * if null painting references too are removed
     */
    fun modifyTile(id: String, displayBuilding: DisplayBuilding?=null){
        tiles.removeIf { it.id==id }
        if(displayBuilding!=null){
            tiles.add(Tile(id, displayBuilding))
        }else{
            tileLocations.removeIf{it.id==id}
        }
    }

    fun getTile(id: String): DisplayBuilding {
        return tiles.first { it.id==id }.db
    }

    fun clearTiles(){
        tiles.clear()
    }

    override fun update() {
        tiles.forEach {
            it.db.update()
        }
    }

    override fun draw(batch: SpriteBatch, drawingRectangle: DrawingRectangle) {
        tileLocations.forEach {
            val l = 1f/cols.toFloat()*(it.col.toFloat()-1f)
            val r = 1f/cols.toFloat()*(it.col.toFloat())
            val t = 1f - ((it.row.toFloat())/rows.toFloat())
            val b = 1f- ((it.row.toFloat()-1)/rows.toFloat())

            tiles.first {it2-> it.id == it2.id }.db.draw(batch,drawingRectangle.ratedCopy(Rectangle(l,r,t,b)))
        }
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        //TODO("Not yet implemented")
    }

    class Tile(val id: String, val db: DisplayBuilding)

    data class TileLocation(val id : String, val row: Int, val col: Int)
}