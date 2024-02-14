package com.gravitycode.solitaryfitnessapp.util

import com.gravitycode.solitaryfitnessapp.BuildConfig
import com.gravitycode.solitaryfitnessapp.util.android.Log

private const val TAG = "DEBUG_ERROR"

/**
 * Throw an error or log an exception if the app is in debug mode, i.e. [BuildConfig.DEBUG] is set to true.
 *
 * If [BuildConfig.CRASH_ON_ERROR] is set to true then the app will crash, if not then an error message will
 * be output to logcat.
 *
 * An optional recovery block can be specified which will be run to attempt to recover from the failed state
 * if the error occurred while in production; such as cleaning up variables, showing a message to the user, etc.
 *
 * @param[message] The message describing the error
 * @param[throwable] The cause of the error if one exists
 * @param[recoveryBlock] The recovery block to be executed if in production mode
 * */
fun error(message: String, throwable: Throwable? = null, recoveryBlock: () -> Unit = {}) {
    if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, throwable)
    } else if (BuildConfig.DEBUG) {
        Log.e(TAG, message, throwable)
    } else {
        recoveryBlock()
    }
}

/**
 * Throw an error or log an exception if the app is in debug mode, i.e. [BuildConfig.DEBUG] is set to true.
 *
 * If [BuildConfig.CRASH_ON_ERROR] is set to true then the app will crash, if not then an error message will
 * be output to logcat.
 *
 * An optional recovery block can be specified which will be run to attempt to recover from the failed state,
 * such as cleaning up variables, showing a message to the user, etc.
 *
 * @param[message] The message describing the error
 * @param[result] The [Result] which an exception will be retrieved from using [Result.exceptionOrNull]
 * @param[recoveryBlock] The recovery block to be executed if no crash occurs.
 * */
fun error(message: String, result: Result<Any?>, recoveryBlock: () -> Unit = {}) {
    error(message, result.exceptionOrNull(), recoveryBlock)
}