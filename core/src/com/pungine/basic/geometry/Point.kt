package com.pungo.modules.basic.geometry

data class Point(val x: Float, val y: Float) {

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun div(other: Float): Point {
        return Point(x / other, y / other)
    }

    operator fun times(other: Float): Point {
        return Point(x * other, y * other)
    }


}