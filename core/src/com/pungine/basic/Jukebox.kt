package com.pungo.modules.basic

import com.badlogic.gdx.Gdx

/** This class has an internal time tracker, which can be activated, and resetted (time is based on libgdx)
 * it must be called by update method in every iteration
 * functions can be added in it, which are then called when jukebox passes the relevant time marker
 */
class Jukebox(private val maxTimeDiff: Float = 10f, private var resetOnFirstCall: Boolean = false) {
    private var timeHolder = 0f
    private val events = mutableListOf<Pair<Float, () -> Unit>>()
    private var maxTime = 0f


    /** Resets time and time Holder
     */
    fun resetAll() {
        timeHolder = 0f
        events.clear()
    }

    /** Resets timer
     */
    fun resetTime() {
        timeHolder = 0f
    }

    /** Adds an event function f to a given time t
     */
    fun addEvent(t: Float, f: () -> Unit) {
        events.add(Pair(t, f))
        maxTime = maxTime.coerceAtLeast(t)
    }


    /** Updates the timer, and stops updating when max time + maxTimeDiff
     */
    fun update() {
        if (resetOnFirstCall) {
            resetTime()
            resetOnFirstCall = false
        } else {
            if (timeHolder < maxTime + maxTimeDiff) {
                val nt = Gdx.graphics.deltaTime
                val e = events.filter { it -> it.first in timeHolder..(timeHolder + nt) }
                e.forEach {
                    it.second()
                }
                timeHolder += nt
            }
        }
    }
}