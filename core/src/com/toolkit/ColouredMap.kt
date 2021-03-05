package com.toolkit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.inputProcessor.InputHandler
import com.pungo.modules.scenes.LayerManager
import com.pungo.modules.scenes.Scene
import com.pungo.modules.visuals.PixmapGenerator
import modules.application.PuniversalValues
import modules.basic.Colours
import modules.simpleUi.Displayer
import modules.simpleUi.SetButton
import modules.simpleUi.TiledDisplay
import modules.simpleUi.text.ColouredTextBox
import modules.simpleUi.text.TextBox
import org.w3c.dom.Text
import kotlin.math.abs

class ColouredMap: Scene("colouredMap") {
    val motherGrid = TiledDisplay(5,5).also {
        it.modifyTile("test",Displayer(Color.RED))
    }
    val tempGrid = TiledDisplay(5,5,).also {
        it.modifyTile("test",Displayer(Color.RED))
    }
    var moveActive = false
    var drawActive = false
    var moving = false
    var initialClick = Point(0f,0f)
    val gridRectangle =  Rectangle(0.15f,0.85f,0.05f,0.95f)

    init {


        mainDistrict.addFullPlot("bg").also {
            it.element = Displayer(Colours.byRGBA256(24,21,35))
        }


        mainDistrict.addFullPlot("gridbg",gridRectangle).also {
            it.element = Displayer(Colours.byRGBA256(64,61,75))
        }

        mainDistrict.addFullPlot("grid",gridRectangle).also {
            it.element = motherGrid
        }

        mainDistrict.addFullPlot("tempGrid",gridRectangle).also {
            it.touchStopper=false
            it.element = tempGrid
        }

        mainDistrict.addFullPlot("cage",gridRectangle).also {
            it.touchStopper = false
            it.element = Displayer(PixmapGenerator.grid(motherGrid.rows,motherGrid.cols))
        }

        mainDistrict.addFullPlot("gridProperties",gridRectangle).also {
            it.visible = false
            it.touchStopper=false
            it.element = GridProperties(motherGrid){row: Int?, col: Int? ->
                val newRow = if(row==null) null else (motherGrid.rows+row).coerceAtLeast(1)
                val newCol = if(col==null) null else (motherGrid.cols+col).coerceAtLeast(1)
                motherGrid.modifySlicing(newRow,newCol)
                tempGrid.modifySlicing(newRow,newCol)
                mainDistrict.findPlot("cage").element = Displayer(PixmapGenerator.grid(motherGrid.rows,motherGrid.cols))
                Pair(motherGrid.rows,motherGrid.cols)
            }
        }

        mainDistrict.splitToPlots("tools",Rectangle(0.01f,0.14f,0.15f,0.95f),6,2).also {
            it[0].element = SetButton(References.buttonTextBox("move",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    it2.inactive = true
                    moveActive = true
                }
            }

            it[1].element = SetButton(References.buttonTextBox("draw",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    it2.inactive = true
                    drawActive = true
                }
            }

            it[2].element = SetButton(References.buttonTextBox("grid size",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    it2.inactive = true
                    mainDistrict.findPlot("gridProperties").visible=true
                }
            }
        }




        mainDistrict.addFullPlot("exitButton", Rectangle(0.01f,0.14f,0.05f,0.1f)).also {
            it.element = SetButton(References.buttonTextBox("back")).also {
                it.clicked = {
                    LayerManager.scenesToRemove.add(this)
                    LayerManager.scenesToAdd.add(Pair(EntryScene(),true))
                }
            }
        }
    }

    private fun deactivateAllButtons(){
        moveActive = false
        drawActive = false
        mainDistrict.findPlots("tools").forEach {
            val e = it.element
            if(e is SetButton){
                e.inactive = false
            }

        }
        mainDistrict.findPlot("gridProperties").visible=false
    }


    var initialBlock: Pair<Int, Int>? = null
    var blockList = mutableListOf<Pair<Int, Int>>()
    override fun update() {
        //blockList.forEach { motherGrid.modifyGrid(null,it.first,it.second) }
        if(drawActive){
            tempGrid.clearGrid()
            val point = mainDistrict.getPunRatedPointOnPlot("tempGrid",PuniversalValues.cursorPoint,true)
            val row = ((1-point.y)*motherGrid.rows-0.0001f).toInt()+1
            val col = (point.x*motherGrid.cols-0.0001f).toInt()+1
            if(mainDistrict.findPlot("tempGrid").hovering){
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    blockList.add(Pair(row,col))
                }
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)&&blockList.isNotEmpty()){
                val initialBlock = blockList[0]
                blockList = if(abs(row-initialBlock.first)>=abs(col-initialBlock.second)){
                    if(row>initialBlock.first){
                        (initialBlock.first..row).map { Pair(it,initialBlock.second) }
                    }else{
                        (row..initialBlock.first).map { Pair(it,initialBlock.second) }.reversed()
                    }
                }else{
                    if(col>initialBlock.second){
                        (initialBlock.second..col).map { Pair(initialBlock.first,it) }
                    }else{
                        (col..initialBlock.second).map { Pair(initialBlock.first,it) }.reversed()
                    }
                }.toMutableList()

            }else{
                blockList.forEach { motherGrid.modifyGrid("test",it.first,it.second) }
                blockList.clear()
            }
        }else{
            blockList.clear()
        }
        blockList.forEach { tempGrid.modifyGrid("test",it.first,it.second) }


        rollFunction()
        moveFunction(moveActive) //do not put it in an if, it needs to update initial click



        mainDistrict.findPlot("cage").zoomRectangle = mainDistrict.findPlot("grid").zoomRectangle
        super.update()
    }

    private fun rollFunction(){
        val grid = mainDistrict.findPlot("cage")
        if(grid.hovering){
            val u =(50f+InputHandler.rotated.toFloat())/50f
            if(u!=1f){
                val initRect = Rectangle(grid.zoomRectangle.width*u,grid.zoomRectangle.height*u,
                    grid.zoomRectangle.centre
                )
                val r = try {
                    FastGeometry.unitSquare().getPushedRectangle(initRect,true)
                }catch (e: Exception){
                    FastGeometry.unitSquare()
                }
                grid.zoomRectangle = r
                mainDistrict.findPlot("grid").zoomRectangle=r
                mainDistrict.findPlot("tempGrid").zoomRectangle =  r
            }
        }
    }

    private fun moveFunction(moveActive: Boolean){
        val grid = mainDistrict.findPlot("grid")
        if(moveActive){
            val clickPoint = mainDistrict.getPunRatedPointOnPlot("grid",PuniversalValues.cursorPoint,true)
            val dx = -1*(clickPoint.x-initialClick.x)
            val dy = -1*(clickPoint.y - initialClick.y)
            val clickRect = Rectangle(grid.zoomRectangle.width,grid.zoomRectangle.height,Point(grid.zoomRectangle.centre.x+dx,grid.zoomRectangle.centre.y+dy))
            if(grid.hovering&&Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                grid.zoomRectangle = FastGeometry.unitSquare().getPushedRectangle(clickRect,true)
                mainDistrict.findPlot("tempGrid").zoomRectangle =  grid.zoomRectangle
            }
        }
        initialClick = FastGeometry.unitSquare().invertSubRectangle(grid.zoomRectangle).getNormalPoint(mainDistrict.getPunRatedPointOnPlot("grid",PuniversalValues.cursorPoint))

    }


}