package com.gravitycode.solitaryfitness.util.test

import org.junit.Assert.*

fun <T> assertSuccess(result: Result<T>, message: String? = null) {
    assertTrue(message, result.isSuccess)
}

fun <T> assertFailure(result: Result<T>, message: String? = null) {
    assertTrue(message, result.isFailure)
}