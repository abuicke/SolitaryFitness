package com.gravitycode.solitaryfitnessapp.util

import com.gravitycode.solitaryfitnessapp.BuildConfig
import com.gravitycode.solitaryfitnessapp.util.android.Log

private const val TAG = "DEBUG_ERROR"

/**
 * Throw an error or log an exception, depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, then a call to [error]
 * will result in an [IllegalStateException] containing the specified [message] and [cause].
 *
 * If only [BuildConfig.DEBUG] is set to `true`, a call to [error] will
 * simply log [message] and [cause] with log level [android.util.Log.ERROR].
 *
 * If neither are set to `true` nothing happens.
 * */
fun error(message: String, cause: Throwable? = null) {
    if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, cause)
    } else {
        Log.e(TAG, message, cause)
    }
}

/**
 * Throw an error or run a recovery block which returns a value, depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, an [IllegalStateException]
 * will be thrown containing the specified [message] and [cause], otherwise [recoveryBlock] will be executed
 * and its return value will be returned from this function.
 *
 * The [message] and [cause] will be logged with log level [android.util.Log.ERROR] along with the execution
 * of the [recoveryBlock] if [BuildConfig.DEBUG] is set to `true`.
 *
 * @param message The message describing the error
 * @param cause The cause of the error if one exists
 * @param recoveryBlock The recovery block to be executed
 * */
fun <T> errorWithRecovery(message: String, cause: Throwable? = null, recoveryBlock: () -> T): T {
    return if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, cause)
    } else if (BuildConfig.DEBUG) {
        Log.e(TAG, message, cause)
        recoveryBlock()
    } else {
        recoveryBlock()
    }
}

/**
 * Throw an error or run a recovery block which returns a value, depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, an [IllegalStateException]
 * will be thrown containing the specified [message] and the exception contained in the [result] as the cause
 * if one exists, otherwise [recoveryBlock] will be executed and its return value will be returned from this
 * function.
 *
 * An error log with accompany the execution of [recoveryBlock] if [BuildConfig.DEBUG] is set to `true`.
 *
 * @param message The message describing the error
 * @param result The [Result] which an exception will be retrieved from using [Result.exceptionOrNull]
 * @param recoveryBlock The recovery block to be executed
 * */
fun <T> errorWithRecovery(message: String, result: Result<Any?>, recoveryBlock: () -> T): T {
    return errorWithRecovery(message, result.exceptionOrNull(), recoveryBlock)
}