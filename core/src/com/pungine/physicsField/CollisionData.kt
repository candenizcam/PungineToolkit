package com.pungo.modules.physicsField

class CollisionData(var id1: String, var id2: String = "") {
    var t: Float = -1f
        set(value) {
            field = value
            if (value < 0) {
                println("NEGATIVE TIME")
            }
        }
    var xCollision = false
    var yCollision = false

    fun setYCollision(t: Float) {
        //println("$id1 and $id2 ARE y colliding")
        this.t = t
        xCollision = false
        yCollision = true
    }

    fun setXCollision(t: Float) {
        //println("$id1 and $id2 ARE x colliding")
        this.t = t
        xCollision = true
        yCollision = false
    }

    fun stuck() {
        //println("OH NO $id1 and $id2 ARE STUCK")
        xCollision = true
        yCollision = true
    }

    fun hasCollision(): Boolean {
        return xCollision || yCollision
    }

    fun isStuck(): Boolean {
        return xCollision && yCollision
    }
}