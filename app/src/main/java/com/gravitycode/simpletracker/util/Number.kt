package com.gravitycode.simpletracker.util

enum class Number(val integer: Int, val float: Float, val double: Double, val string: String) {

    ZERO(0, 0.0F, 0.0,"0")
}

fun Int.isEven() = this % 2 == 0

fun Int.isOdd() = this % 2 == 1