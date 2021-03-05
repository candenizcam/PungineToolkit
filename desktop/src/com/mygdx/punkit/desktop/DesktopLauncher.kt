package com.mygdx.punkit.desktop

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.toolkit.Main
import com.toolkit.References
import modules.application.PuniversalValues

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        PuniversalValues.punWidth = References.appWidth.toFloat()
        PuniversalValues.punHeight = References.appHeight.toFloat()
        config.width = References.appWidth
        config.height = References.appHeight
        LwjglApplication(Main(), config)
    }
}