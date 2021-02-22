package com.pungo.modules.physicsField

class PhysicsItemData(var cX: Float, var cY: Float, var w: Float, var h: Float) {

    fun top(): Float {
        return cY + h / 2
    }

    fun bottom(): Float {
        return cY - h / 2
    }

    fun right(): Float {
        return cX + w / 2
    }

    fun left(): Float {
        return cX - w / 2
    }

    fun translate(dX: Float, dY: Float) {
        cX += dX
        cY += dY
    }

    fun translated(dX: Float, dY: Float): PhysicsItemData {
        return PhysicsItemData(cX + dX, cY + dY, w, h)
    }

    fun relocate(x: Float, y: Float) {
        cX = x
        cY = y
    }

    fun relocated(x: Float, y: Float): PhysicsItemData {
        return PhysicsItemData(x, y, w, h)
    }
}