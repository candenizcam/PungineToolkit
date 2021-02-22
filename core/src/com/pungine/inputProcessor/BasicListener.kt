package com.pungo.modules.inputProcessor

import com.badlogic.gdx.InputProcessor
import com.pungo.modules.scenes.LayerManager

open class BasicListener : InputProcessor {
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        LayerManager.layers.last().mouseMoved(screenX, screenY)
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        LayerManager.layers.last().keyTyped(character)
        InputHandler.typeCache += character
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        LayerManager.layers.last().keyUp(keycode)
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        LayerManager.layers.last().keyDown(keycode)
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        LayerManager.layers.last().touchUp(screenX, screenY)
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        LayerManager.layers.last().touchDragged(screenX, screenY)
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        LayerManager.layers.last().touchDown(screenX, screenY)
        return true
    }
}