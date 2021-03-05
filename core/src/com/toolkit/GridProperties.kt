package com.toolkit

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Point
import modules.basic.Colours
import modules.simpleUi.*
import modules.simpleUi.text.TextBox
import modules.uiPlots.DrawData



class GridProperties(motherGrid: TiledDisplay, val modifyFunction: (row: Int?,col:Int?)->Pair<Int,Int>): Campus() {
    init {

        district.splitToPlots("gridOptions",FastGeometry.unitSquare(),listOf(2f,1f,1f,1f,2f), listOf(1f,2f,1f,1f,1.5f,1f,1f,1f),true).also {
            it[0].element = Displayer(Colours.byRGBA256(7,6,17,240))

            val starter = 9

            it[starter+1].element = References.textBox("rows")
            it[starter+2].element = SetButton(References.buttonTextBox("<<",36)).also { it2->
                it2.clicked = {
                    modifyFunction(-5,null).also {it2->
                        (it[starter+4].element as TextBox).text = it2.first.toString()
                    }

                }
            }
            it[starter+3].element = SetButton(References.buttonTextBox("<",36)).also { it2->
                it2.clicked = {
                    modifyFunction(-1,null).also {it2->
                        (it[starter+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
            it[starter+4].element = References.textBox(motherGrid.rows.toString())
            it[starter+5].element = SetButton(References.buttonTextBox(">",36)).also { it2->
                it2.clicked = {
                    modifyFunction(1,null).also {it2->
                        (it[starter+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
            it[starter+6].element = SetButton(References.buttonTextBox(">>",36)).also { it2->
                it2.clicked = {
                    modifyFunction(5,null).also {it2->
                        (it[starter+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }

            val starter2 = 25
            it[starter2+1].element = References.textBox("cols")
            it[starter2+2].element = SetButton(References.buttonTextBox("<<",36)).also { it2->
                it2.clicked = {
                    modifyFunction(null,-5).also {it2->
                        (it[starter2+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
            it[starter2+3].element = SetButton(References.buttonTextBox("<",36)).also { it2->
                it2.clicked = {
                    modifyFunction(null,-1).also {it2->
                        (it[starter2+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
            it[starter2+4].element = References.textBox(motherGrid.cols.toString())
            it[starter2+5].element = SetButton(References.buttonTextBox(">",36)).also { it2->
                it2.clicked = {
                    modifyFunction(null,1).also {it2->
                        (it[starter2+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
            it[starter2+6].element = SetButton(References.buttonTextBox(">>",36)).also { it2->
                it2.clicked = {
                    modifyFunction(null,5).also {it2->
                        (it[starter2+4].element as TextBox).text = it2.first.toString()
                    }
                }
            }
        }
    }
}