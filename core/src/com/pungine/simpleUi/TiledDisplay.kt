package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import kotlinx.serialization.Serializable
import modules.basic.Colour
import modules.uiPlots.DrawData

class TiledDisplay(cols: Int, rows: Int) :Building {
    var cols: Int = cols
        private set(value) {
            field = value
            tileLocations.removeIf { it.col>field }
        }
    var rows: Int = rows
        private set(value) {
            field=value
            tileLocations.removeIf { it.row>field }
        }

    val tiles = mutableListOf<Tile>()
    val tileLocations = mutableListOf<TileLocation>()

    /** This is the access function for modifying rows and cols
     * filtering is handled at setters
     */
    fun modifySlicing(rows: Int?=null, cols: Int?=null){
        if(rows!=null){
            this.rows = rows
        }
        if(cols!=null){
            this.cols = cols
        }
    }


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

    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        tileLocations.forEach {
            val l = 1f/cols.toFloat()*(it.col.toFloat()-1f)
            val r = 1f/cols.toFloat()*(it.col.toFloat())
            val t = 1f - ((it.row.toFloat())/rows.toFloat())
            val b = 1f- ((it.row.toFloat()-1)/rows.toFloat())
            val tileRectangle = Rectangle(l,r,b,t)
            val limited =  drawData.targetPxRectangle.getIntersection(drawData.drawLimits)
            val dd2 = DrawData(drawData.targetPunRectangle.getSubRectangle(tileRectangle),drawData.expandedFrame.getSubRectangle(tileRectangle),FastGeometry.unitSquare(),limited)
            if(dd2.toBeDrawn()){
                tiles.first {it2-> it.id == it2.id }.db.draw(batch, dd2,alpha)
            }

        }
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        //TODO("Not yet implemented")
    }

    class Tile(val id: String, val db: DisplayBuilding)

    /*
        cr = pixmap colour red
        cg = pixmap colour green
        cb = pixmap colour blue
        ca = pixmap colour alpha

        further properties will be added to this list

        maybe add mode property to tile as well?
     */

    @Serializable
    data class TileLocation(val id : String, val row: Int, val col: Int)
}