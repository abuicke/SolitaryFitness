package com.gravitycode.solitaryfitness.util

import com.gravitycode.solitaryfitness.BuildConfig

fun debugError(message: String? = null, throwable: Throwable? = null) {
    if(BuildConfig.DEBUG) {
        throw IllegalStateException(message, throwable)
    }
}