package com.toolkit

import com.badlogic.gdx.Gdx
import com.pungo.modules.inputProcessor.BasicListener
import com.pungo.modules.inputProcessor.InputHandler
import com.pungo.modules.scenes.LayerManager
import modules.application.PungineAdapter

class Main : PungineAdapter(){
    override fun create() {
        super.create()
        Gdx.input.inputProcessor = BasicListener()
        LayerManager.scenesToAdd.add(Pair(EntryScene(),true))

    }

    override fun render() {
        super.render()
        InputHandler.afterUpdate()
    }
}