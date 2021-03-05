package com.pungo.modules.inputProcessor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object InputHandler {
    var typeCache = ""
    set(value) {
        field=value
        println(value)
    }
    private var backspacePressed = false
    var deletePressed = false
    var rotated: Int = 0


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

    fun numberJustPressed(): String {
        var s = ""
        for(i in 7..16){
            if(Gdx.input.isKeyJustPressed(i)){
                s+= Input.Keys.toString(i)
            }
        }
        return s
    }

    fun numberPressing(): String {
        var s = ""
        for(i in 7..16){
            if(Gdx.input.isKeyPressed(i)){
                s+= Input.Keys.toString(i)
            }
        }
        return s
    }

    fun letterJustPressed(): String{
        var s = ""
        for(i in 29..56){
            if(Gdx.input.isKeyJustPressed(i)){
                s+= Input.Keys.toString(i).let {
                    if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)||Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                        it.toUpperCase()
                    }else{
                        it.toLowerCase()
                    }
                }
            }
        }
        if(Gdx.input.isKeyJustPressed(62)){
            s+= " "
        }
        return s
    }

    fun letterPressed(): String{
        var s = ""
        for(i in 29..56){
            if(Gdx.input.isKeyPressed(i)){
                s+= Input.Keys.toString(i).let {
                    if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)||Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                        it.toUpperCase()
                    }else{
                        it.toLowerCase()
                    }
                }
            }
        }
        if(Gdx.input.isKeyPressed(62)){
            s+= " "
        }
        return s
    }

    fun backspacePressed(): Boolean{
        return Gdx.input.isKeyJustPressed(67)
    }

    /** This update is called in the main class after the update and is used to fix various problems libgdx creates about input
     */
    fun afterUpdate(){
        rotated = 0
    }

}