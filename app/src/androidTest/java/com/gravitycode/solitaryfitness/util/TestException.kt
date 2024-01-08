package com.gravitycode.solitaryfitness.util

class TestException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

fun testError(message: String, cause: Throwable? = null) {
    throw TestException(message, cause)
}