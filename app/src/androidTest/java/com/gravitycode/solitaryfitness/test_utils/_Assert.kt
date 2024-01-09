package com.gravitycode.solitaryfitness.test_utils

import org.junit.Assert.assertTrue

fun <T> assertSuccess(result: Result<T>, message: String? = null) {
    assertTrue(message, result.isSuccess)
}

fun <T> assertFailure(result: Result<T>, message: String? = null) {
    assertTrue(message, result.isFailure)
}