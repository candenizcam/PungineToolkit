package com.toolkit


import com.pungo.modules.basic.geometry.Rectangle
import modules.basic.Colour
import modules.simpleUi.*
import java.lang.Exception

class ColourPicker(punWidth: Float, punHeight: Float, motherGrid: TiledDisplay, editButtonFunction: ()->Unit): Campus() {
    var selectedIndex = 0
    var page = 0

    val motherGrid = motherGrid


    init {
        district.splitToPlots("gridBottom",Rectangle(0f,1f,punWidth/punHeight,0f),3,3).also {
            for(i in 0..8){
                it[i].element = SetButton(Displayer(Colour.rgba256(0,0,0,50)),Displayer(Colour.GREEN)).also {
                    it.setInactive( Displayer(Colour.rgba(0.1f,0.6f,0.2f)))
                    it.clicked={
                        district.findPlots("gridBottom").forEach {
                            (it.element as SetButton).inactive = false
                        }
                        it.inactive=true
                        selectedIndex = i
                    }
                }
            }
        }

        district.splitToPlots("gridTop",Rectangle(0f,1f,punWidth/punHeight,0f),3,3).also {
            for(i in 0..8){
                it[i].touchStopper = false
                it[i].estate = it[i].estate.getSubRectangle(Rectangle(0.1f,0.9f,0.1f,0.9f))
                //it[i].element = Displayer(Colour.RED)
            }
        }

        district.splitToPlots("pick", Rectangle(0f,1f,0.8f,1f),cols=listOf(1f,2f,1f)).also {
            it[0].element = SetButton(References.buttonTextBox("<",24))
            it[1].element = SetButton(References.buttonTextBox("edit",24)).also {
                it.clicked = editButtonFunction
            }
            it[2].element = SetButton(References.buttonTextBox(">",24))
        }

    }

    val selectedColour: Colour
        get() {
            return getGridPair(selectedIndex)
        }


    override fun update() {
        val gridTopList = district.findPlots("gridTop")
        for(i in 0..8){
            if(i<motherGrid.tiles.size){
                gridTopList[i].element = motherGrid.tiles[i].db.copy()
            }else{
                gridTopList[i].element = null
            }
        }
        super.update()
    }

    val selectedTile: TiledDisplay.Tile?
        get() {
            try{
                return motherGrid.tiles[selectedIndex+page*9]
            }catch (e: Exception){
                return null
            }

        }

    fun getGridPair(n: Int): Colour {
        try {
            return (district.findPlots("gridTop")[n].element as DisplayBuilding).getColour()
        }catch (e: Exception){
            return Colour.BLACK
        }

    }


}