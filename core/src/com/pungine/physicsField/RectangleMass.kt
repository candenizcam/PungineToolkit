package com.pungo.modules.physicsField

import kotlin.math.abs

open class RectangleMass(id: String, w: Float, h: Float, override var mass: Float, cX: Float = 0f, cY: Float = 0f, override var vX: Float = 0f, override var vY: Float = 0f, final override var mobility: Boolean) : PhysicsItem(id) {
    override var pid = PhysicsItemData(cX, cY, w, h)


    override fun willCollideX(t: Float, x: Float): Boolean {
        return ((willCollideXAt(x) >= 0) && (willCollideXAt(x) <= t))
    }

    override fun willCollideY(t: Float, y: Float): Boolean {
        return ((willCollideYAt(y) >= 0) && (willCollideYAt(y) <= t))
    }

    override fun willCollideXAt(x: Float): Float {
        val t1 = (x - (pid.cX + pid.w / 2)) / vX
        val t2 = (x - (pid.cX - pid.w / 2)) / vX
        return if ((t1 > 0) && (t2 > 0)) t1
        else if ((t1 < 0) && (t2 < 0)) abs(t2)
        else 0f
    }

    override fun willCollideYAt(y: Float): Float {
        val t1 = (y - (pid.cY + pid.h / 2)) / vY
        val t2 = (y - (pid.cY - pid.h / 2)) / vY
        return if ((t1 > 0) && (t2 > 0)) t1
        else if ((t1 < 0) && (t2 < 0)) abs(t2)
        else 0f
    }


}
