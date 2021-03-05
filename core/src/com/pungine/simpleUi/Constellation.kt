package modules.simpleUi

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.uiPlots.DrawData


/** This replaces former multimedia item with ease of usage
 *
 */
class Constellation(): Building {
    private val stars = mutableListOf<Star>()


    override fun update() {
        stars.forEach {
            it.update()
        }
    }

    /** Add element is the basic unit of constellation,
     *
     */
    fun addElement(b: Building, id: String?=null, rect: Rectangle=FastGeometry.unitSquare(), z: Int?=null){
        stars.removeIf{it.id==id}
        stars.add(Star(id?:"${stars.size}",rect,b,z?: stars.size))
    }

    fun getElement(id: String): Building {
        return stars.first { it.id==id }.building
    }

    fun removeElement(id: String){
        stars.removeIf { it.id==id }
    }

    fun clear(){
        stars.clear()
    }


    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha:Float) {
        stars.sortedBy{ it.z }.forEach {
            val dd2 = DrawData(drawData.targetPunRectangle.getSubRectangle(it.rect),drawData.expandedFrame.getSubRectangle(it.rect),FastGeometry.unitSquare(),drawData.drawLimits)
            if(dd2.toBeDrawn()){
                it.building.draw(batch,dd2,alpha)
            }
        }
    }

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        if(hovering){
            stars.forEach {
                val activate = when {
                    relativePoint==null -> {
                        true
                    }
                    it.rect.contains(relativePoint) -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
                it.building.hoverFunction(activate)
            }
        }


    }

    class Star(val id: String, val rect: Rectangle, val building: Building,val z: Int){
        fun update(){
            building.update()
        }
    }
}