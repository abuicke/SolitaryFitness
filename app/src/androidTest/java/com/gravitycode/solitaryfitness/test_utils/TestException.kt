package dev.gravitycode.solitaryfitness.test_utils

class TestException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

fun testError(message: String, cause: Throwable? = null) {
    throw TestException(message, cause)
}