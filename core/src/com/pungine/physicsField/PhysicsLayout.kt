package com.pungo.modules.physicsField

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.simpleUi.Building
import modules.uiPlots.DrawData

open class PhysicsLayout(var id: String, r: Int, c: Int) : Building {
    val pf = PhysicsField(r, c)

    init {}

    /** Following guys are about adding removing or finding elements from layout
     */
    fun addPhysicsItem(item: PhysicsItem): PhysicsItem {
        if (pf.items.any { it.id == id }) {
            throw Exception("ID clash at add physics item for $id")
        }
        pf.addItem(item)
        return item
    }

    fun removePhysicsItem(id: String) {
        pf.removeItem(id)
    }

    fun findElement(id: String): PhysicsItem {
        return pf.items.first { it.id == id }
    }

    override fun update() {
        pf.update()
    }

    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        pf.items.forEach {
            val e = it.elementPointer
            if (e is Building) {
                val width = it.pid.w/pf.colNo
                val height = it.pid.h/pf.rowNo
                val point = Point(it.pid.cX/pf.colNo,it.pid.cY/pf.rowNo)
                val phRectangle = Rectangle(width,height,point)
                val limited =  drawData.targetPxRectangle.getIntersection(drawData.drawLimits)
                val dd2 = DrawData(drawData.targetPunRectangle.getSubRectangle(phRectangle),drawData.expandedFrame.getSubRectangle(phRectangle),
                    FastGeometry.unitSquare(),limited)
                if(dd2.toBeDrawn()){
                    e.draw(batch,dd2,alpha)
                    //tiles.first {it2-> it.id == it2.id }.db.draw(batch, dd2,alpha)
                }
                //val dr = drawingRectangle.ratedCopy(Rectangle(width,height,point))
                //if(dr.toBeDrawn()){
                //    e.draw(batch,dr,alpha)
                //}
            }
        }
    }

    /*
    override fun draw(batch: SpriteBatch, drawingRectangle: DrawingRectangle, alpha: Float) {
        pf.items.forEach {
            val e = it.elementPointer
            if (e is Building) {
                val width = it.pid.w/pf.colNo
                val height = it.pid.h/pf.rowNo
                val point = Point(it.pid.cX/pf.colNo,it.pid.cY/pf.rowNo)
                val dr = drawingRectangle.ratedCopy(Rectangle(width,height,point))
                if(dr.toBeDrawn()){
                    e.draw(batch,dr,alpha)
                }
            }
        }
    }

     */

    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {
        //TODO("Not yet implemented")
        // this will probably be application spesific
    }


    fun addPhysicsSquare(id: String, row: Float, column: Float, side: Float = 1f, vX: Float = 0f, vY: Float = 0f, mass: Float = 0f, mobility: Boolean): PhysicsItem {
        return addPhysicsItem(RectangleMass(id, w = side, h = side, mass = mass, cX = column + 0.5f, cY = row + 0.5f, vX = vX, vY = vY, mobility = mobility))
    }



    /** Returns items in that physics coordinate
     */
    fun findItems(phiX: Float, phiY: Float, atLine: Boolean = false): List<PhysicsItem> {
        return pf.items.filter {
            Rectangle(it.pid.left(), it.pid.right(), it.pid.bottom(), it.pid.top()).contains(Point(phiX, phiY), atLine)
        }
    }



    /** Returns items in that ratio coordinate
     * recommended usage is with getPunRatedPointOnPlot() function in main district
     */
    fun findItems(point: Point, atLine: Boolean = false): List<PhysicsItem> {
        return findItems(point.x*pf.colNo, point.y*pf.rowNo, atLine)
    }

    /** Returns the normalized centre of a particular item
     */
    fun centreOfAnItem(id: String): Point {
        val pide = pf.items.first{id==it.id }.pid
        return Point(pide.cX/pf.colNo,pide.cY/pf.rowNo)
    }

}
