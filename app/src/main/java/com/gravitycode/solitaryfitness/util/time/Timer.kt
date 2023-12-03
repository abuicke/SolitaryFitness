package com.gravitycode.solitaryfitness.util.time

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun timer(delayMilliseconds: Long) = flow {
    delay(delayMilliseconds)
    emit(Unit)
}