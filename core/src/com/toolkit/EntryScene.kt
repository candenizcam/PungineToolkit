package com.toolkit


import com.pungo.modules.basic.geometry.Rectangle
import com.pungo.modules.scenes.LayerManager
import com.pungo.modules.scenes.Scene
import modules.basic.Colour

import modules.simpleUi.Displayer
import modules.simpleUi.SetButton

class EntryScene: Scene( "Entry", zOrder = 0f) {
    init {
        mainDistrict.addFullPlot("bg").also {
            it.element = Displayer(Colour.rgba256(24,21,35))
        }

        mainDistrict.addFullPlot("colouredMapButton",Rectangle(0.1f,0.3f,0.7f,0.8f)).also {
            it.element = SetButton(References.buttonTextBox("Coloured Map")).also {
                it.clicked = {
                    LayerManager.scenesToRemove.add(this)
                    LayerManager.scenesToAdd.add(Pair(ColouredMap(),true))
                }
            }
        }

        /*
        mainDistrict.addFullPlot("exitButton", Rectangle(0.01f,0.11f,0.01f,0.11f)).also {
            it.element = SetButton(References.buttonTextBox("Exit")).also {
                it.clicked = {
                    LayerManager.dispose()
                    Gdx.app.exit()
                }
            }
        }

         */

    }
}