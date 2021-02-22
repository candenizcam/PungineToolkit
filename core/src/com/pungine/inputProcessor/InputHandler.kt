package com.pungo.modules.inputProcessor

object InputHandler {
    var typeCache = ""
    private var backspacePressed = false
    var deletePressed = false
    fun getTypeCache(erase: Boolean = true): String {
        return typeCache.also {
            if (erase) typeCache = ""
        }
    }

    fun listenBackspace(erase: Boolean): Boolean {
        return backspacePressed.also {
            if (erase) backspacePressed = false
        }
    }

}