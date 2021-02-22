package com.pungo.modules.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

object MusicPlayer {
    lateinit var bgm: Music
    var volume = 1.0f
    var muted = false

    fun open(fileName: String) {
        val file = Gdx.files.internal(fileName)
        try {
            bgm.dispose()
        } catch (ex: UninitializedPropertyAccessException) {

        } finally {
            bgm = Gdx.audio.newMusic(file)
            if (muted) {
                bgm.volume = 0f
            } else {
                bgm.volume = volume
            }
        }

    }

    fun setLooping(loop: Boolean) {
        bgm.isLooping = loop
    }

    fun play() {
        bgm.play()
    }

    fun pause() {
        bgm.pause()
    }

    fun stop() {
        bgm.stop()
    }

    fun dispose() {
        bgm.dispose()
    }

    fun changeVolume(vol: Float) {
        if (!muted) {
            volume = vol
            bgm.volume = vol
        } else {
            bgm.volume = 0.0f
        }
    }

    fun mute(muted: Boolean) {
        this.muted = muted
        if (muted) {
            bgm.volume = 0.0f
        } else {
            bgm.volume = volume
        }
    }
}