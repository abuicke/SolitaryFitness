package com.gravitycode.solitaryfitness.util.error

import com.gravitycode.solitaryfitness.util.android.Log
import com.gravitycode.solitaryfitness.BuildConfig
import com.gravitycode.solitaryfitness.app.SolitaryFitnessApp

fun debugError(message: String, throwable: Throwable? = null) {
    if (BuildConfig.DEBUG && SolitaryFitnessApp.CRASH_ON_DEBUG_ERROR) {
        throw IllegalStateException(message, throwable)
    } else if (BuildConfig.DEBUG) {
        Log.e("DEBUG_ERROR", message, throwable)
    }
}

fun debugError(message: String, result: Result<Any?>) {
    debugError(message, result.exceptionOrNull())
}

/**
 * Throws an [IllegalStateError] if the specified condition is `false`.
 *
 * @param[condition] The condition to test
 * */
fun assertState(condition: Boolean, message: String? = null) {
    if (!condition) {
        throw IllegalStateError(message)
    }
}