package com.pungo.modules.uiPlots

import com.badlogic.gdx.Gdx
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import modules.simpleUi.Building

open class Plot(val id: String, var estate: Rectangle = FastGeometry.unitSquare(), var z: Int = 0, element: Building? = null, var zoomRectangle: Rectangle = FastGeometry.unitSquare()) {
    var visible = true
        set(value) {
            field = value
            //element?.visible = value
        }
    var locations = Locations().also {
        it.add(estate.copy())
    }
    var hovering = false
    var inactive = false //handled on district level
    var touchStopper = true // if false things behind this can be touched
    var element: Building? = element



    fun merge(mergedId: String, other: Plot, z: Int? = null): Plot {
        return Plot(mergedId, estate + other.estate, z ?: (this.z).coerceAtLeast(other.z))
    }


    fun gridEqual(tag: String, r: Int = 1, c: Int = 1): MutableList<Plot> {
        val returning = mutableListOf<Plot>()
        repeat(r) { ri ->
            repeat(c) { ci ->
                val left = estate.left + estate.width / c * ci
                val right = estate.left + estate.width / c * (ci + 1)
                val top = estate.top - estate.height / r * (ri + 1)
                val bottom = estate.top - estate.height / r * (ri)
                val rect = Rectangle(left, right, top, bottom)
                val plot = Plot("${tag}_r${ri+1}_c${ci+1}", rect, z + 1)
                returning.add(plot)
            }
        }
        return returning
    }


    fun gridBiased(tag: String, r: List<Float>, c: List<Float>): MutableList<Plot> {
        val nRows = r.map { it / r.sum() }
        val nCols = c.map { it / c.sum() }
        val returning = mutableListOf<Plot>()
        var rowAcc = estate.top
        nRows.forEachIndexed { ri, row ->
            var colAcc = estate.left
            val bottom = rowAcc - row * estate.height
            val top = rowAcc
            nCols.forEachIndexed { ci, col ->
                val left = colAcc
                val right = colAcc + col * estate.width
                colAcc += col * estate.width
                val plot = Plot("${tag}_r${ri + 1}_c${ci + 1}", Rectangle(left, right, top, bottom), z + 1)
                returning.add(plot)
            }
            rowAcc -= row * estate.height

        }
        return returning


    }

    fun update(relativePoint: Point) {
        element?.hoverFunction(hovering,relativePoint)
        element?.update()
        if (locations.moving) {
            estate = locations.getEstate()
        }
    }

    fun copy(id: String = this.id, z: Int = this.z): Plot {
        return Plot(id, estate, z, element)
    }

    /** Moves this plot for dx and dy as floats
     *
     */
    fun movePlot(dx: Float = 0f, dy: Float = 0f) {
        estate.move(dx, dy)
    }

    fun moveBetween(to: Int, time: Float) {
        if (!locations.moving) {
            locations.setMovement(to, time)
        }
    }

    fun moveToNextLocation(time: Float) {
        if (!locations.moving) {
            var n = locations.from + 1
            if (n == locations.locations.size) {
                n = 0
            }
            locations.setMovement(n, time)
        }

    }

    class Locations {
        var locations = mutableListOf<Rectangle>()
        var moveTime = 0f
        var moveTimer = 0f
        var moving = false
        var from = 0
        var to = 0
        var startingFunction = {}
        var endingFunction = {}


        fun add(r: Rectangle) {
            locations.add(r)
        }

        fun getEstate(): Rectangle {
            moveTimer -= Gdx.graphics.deltaTime
            return if (moveTimer <= 0) {
                moveTimer = 0f
                from = to
                endingFunction()
                moving = false
                locations[from].copy()
            } else {
                moving = true
                locations[from].averageRect(locations[to], 1f - moveTimer / moveTime)
            }
        }

        fun setMovement(target: Int, time: Float) {
            if (target != from) {
                to = target
                moving = true
                moveTime = time
                moveTimer = time
                startingFunction()
            }
        }


    }

}