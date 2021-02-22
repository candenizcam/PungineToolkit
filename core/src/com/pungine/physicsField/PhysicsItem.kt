package com.pungo.modules.physicsField


import modules.simpleUi.Building
import kotlin.math.abs

abstract class PhysicsItem(var id: String) {
    val physicsResolution = 1000
    abstract var mobility: Boolean
    abstract var mass: Float
    abstract var pid: PhysicsItemData
    abstract var vX: Float
    abstract var vY: Float
    var collidable = true
    var lasered = false
    open var elasticityFactor = 1f
    var flying = false
    open var elementPointer: Building? = null

    /**Returns potential pair of cX cY
     *
     */

    open fun anticipateMovement(t: Float): Pair<Float, Float> {
        return Pair(pid.cX + t * vX, pid.cY + t * vY)
    }

    open fun translate(x: Float = 0f, y: Float = 0f) {
        pid.translate(x, y)
    }

    open fun moveTo(x: Float, y: Float) {
        pid.relocate(x, y)
    }

    open fun addMomentum(pX: Float, pY: Float) {
        if (mass != 0f) {
            vX += pX / mass
            vY += pY / mass
        }
    }

    open fun stop(vertical: Boolean = false, horizontal: Boolean = false) {
        if (vertical) vY = 0f
        if (horizontal) vX = 0f
    }

    open fun translate(t: Float) {
        translate(vX * t, vY * t)
    }

    open fun jump(jumpMomentMult: Float) {
        addMomentum(0f, mass * jumpMomentMult)
    }

    open fun update() {
        elementPointer?.update()

    }

    abstract fun willCollideX(t: Float, x: Float): Boolean

    abstract fun willCollideY(t: Float, y: Float): Boolean

    abstract fun willCollideXAt(x: Float): Float
    abstract fun willCollideYAt(y: Float): Float


    fun makeImmobile() {
        mobility = false

    }

    fun makeMobile(mass: Float) {
        mobility = true
        this.mass = mass
    }

    /** Returns collision time with another object
     * This will work slow
     * But it will work
     * I hope.
     */
    fun collisionTimeWith(p: PhysicsItem, lt: Float): CollisionData {
        val collisionData = CollisionData(id, p.id)
        if (overlapsAtXAfter(p, 0f) && overlapsAtYAfter(p, 0f)) {
            collisionData.stuck()
        } else if (!(overlapsAtXAfter(p, lt) && overlapsAtYAfter(p, lt))) {
            return collisionData
        } else {
            var xOverlaps: Boolean
            var yOverlaps = overlapsAtYAfter(p, 0f)
            for (i in 1..100) {
                val time = lt / 100f * i.toFloat()
                xOverlaps = overlapsAtXAfter(p, time)
                if (xOverlaps && yOverlaps) {
                    collisionData.setXCollision(time - lt / 100f)
                    break
                }
                yOverlaps = overlapsAtYAfter(p, time)
                if (xOverlaps && yOverlaps) {
                    collisionData.setYCollision(time - lt / 100f)
                    break
                }


            }


        }
        return collisionData
    }

    private fun overlapsAtXAfter(other: PhysicsItem, t: Float): Boolean {
        return !(pid.cX + vX * t - (pid.w / 2) >= other.pid.cX + other.vX * t + (other.pid.w / 2) || other.pid.cX + other.vX * t - (other.pid.w / 2) >= pid.cX + vX * t + (pid.w / 2))
    }

    private fun overlapsAtYAfter(other: PhysicsItem, t: Float): Boolean {
        return !(pid.cY + vY * t + (pid.h / 2) <= other.pid.cY + other.vY * t - (other.pid.h / 2) || other.pid.cY + other.vY * t + (other.pid.h / 2) <= pid.cY + vY * t - (pid.h / 2))
    }

    /** Collide sets velocities of colliding particles to the resolved collision values
     * collision is between this and the other
     * collision data describes the nature of the collision
     * elasticity is a multiplier to the resulting velocities, 1 is full elastic 0 is full plastic but in between may not be as intuitive
     */
    open fun collide(other: PhysicsItem, c: CollisionData, elasticity: Float = 1f) {
        if (c.xCollision) xCollide(other, elasticity)
        if (c.yCollision) yCollide(other, elasticity)
    }

    open fun xCollide(other: PhysicsItem, elasticity: Float = 1f) {
        if (!mobility) {
            other.vX = -other.vX * elasticity * other.elasticityFactor
        } else if (!other.mobility) {
            vX = -vX * elasticity * elasticityFactor
        } else {
            val ux1 = vX
            val ux2 = other.vX
            val thisVx = ((mass - other.mass) / (mass + other.mass) * ux1 + 2 * other.mass / (mass + other.mass) * ux2) * elasticity * elasticityFactor
            val otherVx = (2 * mass / (mass + other.mass) * ux1 + (other.mass - mass) / (mass + other.mass) * ux2) * elasticity * other.elasticityFactor
            vX = if (abs(thisVx) > 0.01f) thisVx else 0f
            other.vX = if (abs(otherVx) > 0.01f) otherVx else 0f
        }
    }

    open fun yCollide(other: PhysicsItem, elasticity: Float = 1f) {
        if (!mobility) {
            other.vY = -other.vY * elasticity * other.elasticityFactor
        } else if (!other.mobility) {
            vY = -vY * elasticity * elasticityFactor
        } else {
            val uy1 = vY
            val uy2 = other.vY
            val thisVy = ((mass - other.mass) / (mass + other.mass) * uy1 + 2 * other.mass / (mass + other.mass) * uy2) * elasticity * elasticityFactor
            val otherVy = (2 * mass / (mass + other.mass) * uy1 + (other.mass - mass) / (mass + other.mass) * uy2) * elasticity * other.elasticityFactor
            vY = if (abs(thisVy) > 0.01f) thisVy else 0f
            other.vY = if (abs(otherVy) > 0.01f) otherVy else 0f
        }
        if (pid.cY > other.pid.cY) {
            flying = false
        }
        if (other.pid.cY > pid.cY) {
            other.flying = false
        }

    }

    fun unstuck(other: PhysicsItem) {
        var velX = pid.cX - other.pid.cX
        var velY = pid.cY - other.pid.cY
        if (velX == 0f && velY == 0f) {
            velX = 0.01f * (pid.w + other.pid.w) / (pid.w + other.pid.w + pid.h + other.pid.h)
            velY = 0.01f * (pid.h + other.pid.h) / (pid.w + other.pid.w + pid.h + other.pid.h)
        }
        for (i in 0..100) {
            if (mobility) {
                translate(velX * 0.001f, velY * 0.001f)
            }
            if (other.mobility) {
                other.translate(-velX * 0.001f, -velY * 0.001f)
            }
            if (!collisionTimeWith(other, 0f).isStuck()) break
        }
    }

    fun getRow(): Float {
        return pid.cY - 0.5f
    }

    fun getCol(): Float {
        return pid.cX - 0.5f
    }
}