package com.pungo.modules.basic.geometry

object FastGeometry {
    fun unitSquare(): Rectangle { //returns a 0->1,0->1 rectangle
        return Rectangle(0f, 1f, 0f, 1f)
    }
}