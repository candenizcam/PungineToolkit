package com.pungo.modules.scenes

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import modules.application.PuniversalValues
import modules.uiPlots.SceneDistrict

/** Scenes are used to arrange and use districts
 *
 */
open class Scene(val id: String, var zOrder: Float,
                 intendedWidth: Float=PuniversalValues.punWidth,
                 intendedHeight: Float = PuniversalValues.punHeight,
                 sceneScaling: SceneDistrict.ResizeReaction = SceneDistrict.ResizeReaction.STRETCH) {
    var visible = true
    var mainDistrict = SceneDistrict("${id}_district", intendedWidth,intendedHeight,sceneScaling)
        private set

    open fun entering() {

    }

    open fun draw(batch: SpriteBatch) {
        if (visible) {
            mainDistrict.draw(batch)
        }

    }

    open fun update() {
        if (visible) {
            mainDistrict.update()
        }
    }


    open fun dispose() {

    }

    open fun mouseMoved(screenX: Int, screenY: Int) {

    }

    open fun keyTyped(character: Char) {

    }

    open fun keyUp(keycode: Int) {

    }

    open fun keyDown(keycode: Int) {

    }

    open fun touchUp(screenX: Int, screenY: Int) {

    }

    open fun touchDragged(screenX: Int, screenY: Int) {

    }

    open fun touchDown(screenX: Int, screenY: Int) {

    }
}