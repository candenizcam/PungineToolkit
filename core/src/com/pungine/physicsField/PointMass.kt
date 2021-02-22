package com.pungo.modules.physicsField

class PointMass(id: String, override var mass: Float, cX: Float = 0f, cY: Float = 0f, override var vX: Float = 0f, override var vY: Float = 0f) : PhysicsItem(id) {
    override var mobility: Boolean = mass != 0f
    override var pid = PhysicsItemData(cX, cY, 0f, 0f)


    override fun willCollideX(t: Float, x: Float): Boolean {
        return (willCollideXAt(x) <= t) && (willCollideXAt(x) >= 0)
    }

    override fun willCollideY(t: Float, y: Float): Boolean {
        return (willCollideYAt(y) <= t) && (willCollideYAt(y) >= 0)
    }

    override fun willCollideXAt(x: Float): Float {
        return (x - pid.cX) / vX
    }

    override fun willCollideYAt(y: Float): Float {
        return (y - pid.cY) / vY
    }


}