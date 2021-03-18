package com.toolkit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.inputProcessor.InputHandler
import com.pungo.modules.scenes.LayerManager
import com.pungo.modules.scenes.Scene
import com.pungo.modules.visuals.PixmapGenerator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import modules.application.PuniversalValues
import modules.basic.Colour
import modules.simpleUi.Displayer
import modules.simpleUi.SetButton
import modules.simpleUi.TiledDisplay
import modules.uiPlots.SceneDistrict
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.filechooser.FileNameExtensionFilter

class ColouredMap: Scene("colouredMap",sceneScaling = SceneDistrict.ResizeReaction.RATED) {
    val motherGrid = TiledDisplay(5,5).also {
        it.modifyTile("test",Displayer(Colour.RED))
        //it.modifyTile("test",Displayer(Color.RED))
        it.tileData.add(TiledDisplay.TileData("test", TiledDisplay.BrushTypes.PIXMAP, 150,150,150,100))
        it.tileDataToTile()
    }
    val tempGrid = TiledDisplay(5,5,).also {
        it.modifyTile("test",Displayer(Colour.RED))
        it.modifyTile("eraser",Displayer(Colour.rgba256(150,150,150,100)))
        //it.modifyTile("test",Displayer(Color.RED))
        it.tileData.add(TiledDisplay.TileData("test", TiledDisplay.BrushTypes.PIXMAP, 150,150,150,100))
        it.tileData.add(TiledDisplay.TileData("eraser",TiledDisplay.BrushTypes.PIXMAP, 150,150,150,100))
        //it.modifyTile("eraser",Displayer(Colours.byRGBA256(150,150,150,100)))
        it.tileDataToTile()
    }
    var moveActive = false
    var drawActive = false
    var eraserActive = false
    var moving = false
    var initialClick = Point(0f,0f)
    val gridRectangle =  Rectangle(0.15f,0.85f,0.05f,0.95f)
    var activeTile = "test"
    var drawingStyle = DrawingStyle.PENCIL

    init {


        mainDistrict.addFullPlot("bg").also {
            it.element = Displayer(Colour.rgba256(24,21,35))
        }


        mainDistrict.addFullPlot("gridbg",gridRectangle).also {
            it.element = Displayer(Colour.rgba256(64,61,75))
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

        mainDistrict.addFullPlot("colourEditor",gridRectangle).also {
            it.visible = false
            it.touchStopper = false
            it.element = ColourEditor()
        }



        mainDistrict.splitToPlots("drawing options",Rectangle(0.86f,0.99f,0.85f,0.95f),1,3).also {
            it[0].element = SetButton(References.buttonTextBox("pencil",18),0.8f,0.8f).also {it2->
                it2.inactive=true
                it2.clicked = {
                    it.forEach { it3-> (it3.element as SetButton).inactive = false }
                    it2.inactive = true
                    drawingStyle = DrawingStyle.PENCIL
                }
            }
            it[1].element = SetButton(References.buttonTextBox("line",18),0.8f,0.8f).also {it2->
                it2.clicked = {
                    it.forEach { it3-> (it3.element as SetButton).inactive = false }
                    it2.inactive = true
                    drawingStyle = DrawingStyle.LINE
                }
            }
            it[2].element = SetButton(References.buttonTextBox("area",18),0.8f,0.8f).also {it2->
                it2.clicked = {
                    it.forEach { it3-> (it3.element as SetButton).inactive = false }
                    it2.inactive = true
                    drawingStyle = DrawingStyle.AREA
                }
            }
        }

        mainDistrict.addFullPlot("colourPicker",Rectangle(0.86f,0.99f,0.40f,0.73f)).also {
            it.element = ColourPicker(PuniversalValues.appWidth*0.13f,PuniversalValues.appHeight*0.33f,motherGrid){

                val p = mainDistrict.findPlot("colourEditor")
                p.visible = if(p.visible){
                    false
                }else{
                    val st = (mainDistrict.findPlot("colourPicker").element as ColourPicker).selectedTile
                    if(st!=null){
                        (p.element as ColourEditor).activate(st.id,st.db.getColour())
                    }else{
                        (p.element as ColourEditor).activate(null,Colour.BLACK)
                    }
                    true
                }
            }
        }

        toolsButtons()
        mainDistrict.addFullPlot("exitButton", Rectangle(0.01f,0.14f,0.05f,0.1f)).also {
            it.element = SetButton(References.buttonTextBox("back")).also {
                it.clicked = {
                    LayerManager.scenesToRemove.add(this)
                    LayerManager.scenesToAdd.add(Pair(EntryScene(),true))
                }
            }
        }
    }

    private fun toolsButtons(){
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

            it[3].element = SetButton(References.buttonTextBox("eraser",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    it2.inactive = true
                    eraserActive= true
                }
            }

            it[10].element = SetButton(References.buttonTextBox("save",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    Gdx.app.postRunnable {
                        val filechooser = JFileChooser()
                        filechooser.currentDirectory = Gdx.files.local("/maps").file()
                        filechooser.fileFilter = FileNameExtensionFilter("Map Files", "map")
                        val f = JFrame()
                        f.isVisible = true
                        f.toFront()
                        f.isVisible = false
                        val res = filechooser.showSaveDialog(f)
                        f.dispose()
                        if (res == JFileChooser.APPROVE_OPTION) {
                            saveMap(filechooser.selectedFile.nameWithoutExtension)
                        }
                    }
                }
            }

            it[11].element = SetButton(References.buttonTextBox("load",24),0.8f,0.8f).also {it2->
                it2.clicked = {
                    deactivateAllButtons()
                    Gdx.app.postRunnable {
                        val filechooser = JFileChooser()
                        filechooser.currentDirectory = Gdx.files.local("/maps").file()
                        filechooser.fileFilter = FileNameExtensionFilter("Map Files", "map")
                        val f = JFrame()
                        f.isVisible = true
                        f.toFront()
                        f.isVisible = false
                        val res = filechooser.showOpenDialog(f)
                        f.dispose()
                        if (res == JFileChooser.APPROVE_OPTION) {
                            loadMap(filechooser.selectedFile.nameWithoutExtension)
                        }
                    }
                }
            }
        }
    }

    private fun saveMap(name: String){
        val json = Json
        val map = json.encodeToString<List<TiledDisplay.TileLocation>>(motherGrid.tileLocations)
        val file = Gdx.files.local("maps/$name.map")
        file.writeString(map, false)
    }

    private fun loadMap(name: String){
        val json = Json
        val file = Gdx.files.local("maps/$name.map")
        val map = file.readString()
        motherGrid.tileLocations.clear()
        motherGrid.tileLocations.addAll(json.decodeFromString<List<TiledDisplay.TileLocation>>(map))
    }

    private fun deactivateAllButtons(){
        moveActive = false
        drawActive = false
        eraserActive = false
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
        drawingFunction()
        rollFunction()
        moveFunction(moveActive) //do not put it in an if, it needs to update initial click
        val ce = (mainDistrict.findPlot("colourEditor").element as ColourEditor)
        if(ce.isRecordClicked()){
            mainDistrict.findPlot("colourEditor").visible = false
            val p = ce.getRecording()
            if(p.first!=null){
                val d = if(p.second==null){
                    null
                }else{
                    Displayer(p.second!!)
                }
                motherGrid.modifyTile(p.first!!,d)
            }
        }



        mainDistrict.findPlot("cage").zoomRectangle = mainDistrict.findPlot("grid").zoomRectangle
        super.update()
    }

    private fun drawingFunction(){
        val st = (mainDistrict.findPlot("colourPicker").element as ColourPicker).selectedTile

        if(drawActive||eraserActive){
            tempGrid.clearGrid()
            val point = mainDistrict.getPunRatedPointOnPlot("tempGrid",PuniversalValues.cursorPoint,true)
            val row = ((1-point.y)*motherGrid.rows-0.0001f).toInt()+1
            val col = (point.x*motherGrid.cols-0.0001f).toInt()+1
            if(mainDistrict.findPlot("tempGrid").hovering){
                when(drawingStyle){
                    DrawingStyle.PENCIL->{
                        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                            val thing = if(drawActive&&(st!=null)) st.id else null
                            motherGrid.modifyGrid(thing,row,col)
                        }
                    }
                    else->{
                        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                            blockList.add(Pair(row,col))
                        }

                        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)&&blockList.isNotEmpty()){
                            val initialBlock = blockList[0]
                            val rowRange = if(row>initialBlock.first){
                                (initialBlock.first..row)
                            }else{
                                (row..initialBlock.first).reversed()
                            }
                            val colRange = if(col>initialBlock.second){
                                (initialBlock.second..col)
                            }else{
                                (col..initialBlock.second).reversed()
                            }
                            blockList = drawingStyle.blockList(rowRange.toList(),colRange.toList())
                        }else{
                            val thing = if(drawActive) "test" else null
                            blockList.forEach { motherGrid.modifyGrid(thing,it.first,it.second)}
                            blockList.clear()
                        }

                    }
                }
            }
        }else{
            blockList.clear()
        }


        if(drawActive&&(st!=null)){
            blockList.forEach { tempGrid.modifyGrid(st.id,it.first,it.second) }
        }else if (eraserActive){
            blockList.forEach { tempGrid.modifyGrid("eraser",it.first,it.second) }
        }
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

    enum class DrawingStyle{
        PENCIL {
            override fun blockList(rowRange: List<Int>, colRange: List<Int>):  MutableList<Pair<Int, Int>> {
                return mutableListOf<Pair<Int,Int>>()
            }
        },
        LINE {
            override fun blockList(rowRange: List<Int>, colRange: List<Int>):  MutableList<Pair<Int, Int>> {
                return if(rowRange.size>=colRange.size){
                    rowRange.map { Pair(it,colRange[0]) }
                }else{
                    colRange.map { Pair(rowRange[0],it) }
                }.toMutableList()
            }
        },
        AREA {
            override fun blockList(rowRange: List<Int>, colRange: List<Int>): MutableList<Pair<Int, Int>> {
                val toBeReturned = mutableListOf<Pair<Int,Int>>()
                for (i in rowRange){
                    for (j in colRange){
                        toBeReturned.add(Pair(i,j))
                    }
                }
                return toBeReturned
            }
        };

        abstract fun blockList(rowRange: List<Int>, colRange: List<Int>): MutableList<Pair<Int, Int>>
    }


}