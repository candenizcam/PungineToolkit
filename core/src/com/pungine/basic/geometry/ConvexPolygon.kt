package com.pungo.modules.basic.geometry

open class ConvexPolygon {
    var centre = Point(0f, 0f) //automatically updated with changing points, and should be left to that, do not change at any other place

    var points = mutableListOf<Point>()
        set(value) {
            field = value
            var p0 = Point(0f, 0f)
            value.forEach {
                p0 += it
            }
            centre = p0 / points.size.toFloat()
        }

    //constructor(sides: Int, sideLength: Float){}
    constructor(l: MutableList<Point>) {
        points = l
    }

    protected constructor()


    fun contains(p: Point, atLine: Boolean = false): Boolean {
        for (i in 0..(points.size) - 2) {

            val l = Line(points[i], points[i + 1])
            val ar = l.atLeft(p, atLine)
            val cr = l.atLeft(centre)
            if (ar != cr) {
                return false
            }
        }
        val l = Line(points.last(), points[0])
        val ar = l.atLeft(p, atLine)
        val cr = l.atLeft(centre)
        if (ar != cr) {
            return false
        }
        return true
    }

}