package com.pungo.modules.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

object SfxPlayer {
    var volume: Float = 1.0f
    private val sfxList = mutableListOf<Pair<String, Sound>>()
    var muted = false

    fun addSFX(name: String, fileName: String) {
        val sfx = Gdx.audio.newSound(Gdx.files.internal(fileName))
        sfxList.add(Pair(name, sfx))
    }

    fun loop(name: String) {
        if (!muted) {
            sfxList.filter { it.first == name }[0].second.loop(volume)
        }
    }

    fun play(name: String) {
        if (!muted) {
            sfxList.filter { it.first == name }[0].second.play(volume)
        }
    }

    fun stop(name: String) {
        try {
            sfxList.filter { it.first == name }[0].second.stop()
        } catch (e: Exception) {
            //does nothing
        }
    }

    fun changeVolume(vol: Float) {
        volume = if (!muted) {
            vol
        } else {
            0.0f
        }
    }
}