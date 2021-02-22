package com.pungo.modules.scenes

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import modules.visuals.FontGenerator
import modules.visuals.TextureCache

object LayerManager {
    val layers = mutableListOf<Scene>()
    val scenesToAdd = mutableListOf<Pair<Scene, Boolean>>()
    val scenesToRemove = mutableListOf<Scene>()

    fun add(scene: Scene, visible: Boolean) {
        if (layers.any { it.id == scene.id }) {
            throw Exception("A scene with this id already exists: " + scene.id)
        } else {
            scene.visible = visible
            layers.add(scene)
        }
    }

    fun findScene(id: String): Scene {
        val found = layers.filter { it.id == id }
        when {
            found.isEmpty() -> {
                throw SceneNotFoundException("No scene with that name")
            }
            found.size == 1 -> {
                return found[0]
            }
            else -> {
                throw Exception("There are multiple scenes by that name")
            }
        }
    }

    fun update() {
        layers.sortWith(compareBy({ it.visible }, { it.zOrder }))
        layers.forEach { it.update() }

        scenesToRemove.forEach {
            layers.remove(it)
        }
        scenesToAdd.forEach {
            add(it.first, it.second)
            it.first.entering()
        }
        scenesToAdd.clear()
        scenesToRemove.clear()
    }

    fun draw(batch: SpriteBatch) {
        layers.forEach { if (it.visible) it.draw(batch) }
    }

    fun dispose() {
        layers.forEach {
            it.dispose()
        }
        FontGenerator.dispose()
        TextureCache.dispose()
    }
}