package com.pungo.modules.basic.math

import kotlin.math.ceil
import kotlin.math.floor

object DoMathToFind {
    fun listOfIntsBetweenFloats(f1: Float, f2: Float): IntRange {
        val minF = f1.coerceAtMost(f2)
        val maxF = f1.coerceAtLeast(f2)
        return IntRange(ceil(minF).toInt(), floor(maxF).toInt())
    }
}