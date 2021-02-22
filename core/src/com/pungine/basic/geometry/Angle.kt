package com.pungo.modules.basic.geometry

import kotlin.math.cos
import kotlin.math.sin

/** Angle class is used to handle angles
 * v is a value described futher by the inner enum class Type
 * Type.RATIO v is a ratio between 0,1
 * Type.RAD v is a radian value between 0,2*pi(3.141)
 * Type.DEG v is a degree between 0,360
 */
data class Angle(private val v: Float, private val type: Type= Type.RATIO){
    val ratio = when(type){
        Type.RAD -> {v/3.1416f/2}
        Type.DEG -> v/360f
        Type.RATIO -> v
    }
    val radian: Float
        get() {
            return ratio*3.1416f*2
        }

    val deg: Float
        get() {
            return ratio*360f
        }

    operator fun plus(other: Angle): Angle {
        return Angle(this.ratio+other.ratio)
    }

    operator fun times(other: Float): Angle{
        return Angle(this.ratio*other)
    }

    operator fun div(other: Angle): Float{
        return this.ratio/other.ratio
    }

    fun rotateVector(x: Float, y: Float): Pair<Float, Float> {
        val x2 = x*cos(radian) - y*sin(radian)
        val y2 = x*sin(radian) + y*cos(radian)
        return Pair(x2,y2)
    }


    enum class Type{
        RAD, RATIO, DEG
    }
}
