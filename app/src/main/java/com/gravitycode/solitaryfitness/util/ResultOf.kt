package com.gravitycode.solitaryfitness.util

class ResultOf<T> private constructor(
    val subject: T,
    val isSuccess: Boolean,
    val exception: Throwable?
) {

    val isFailure: Boolean = !isSuccess

    @Suppress("BooleanLiteralArgument")
    companion object {

        fun <T> success(t: T) = ResultOf(t, true, null)

        fun <T> failure(t: T, exception: Throwable) = ResultOf(t, false, exception)
    }
}