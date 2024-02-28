package com.gravitycode.solitaryfitnessapp.util

import com.gravitycode.solitaryfitnessapp.BuildConfig
import com.gravitycode.solitaryfitnessapp.util.android.Log

private const val TAG = "DEBUG_ERROR"

/**
 * Throws an error or logs an exception based on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`,
 * throws an [IllegalStateException] with the specified [message] and [cause].
 *
 * If only [BuildConfig.DEBUG] is set to `true`, logs [message] and [cause] at log level [android.util.Log.ERROR].
 *
 * If neither are set to `true` nothing happens.
 *
 * @param message The message describing the error
 * @param cause The cause of the error if one exists
 * */
fun error(message: String, cause: Throwable? = null) {
    if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, cause)
    } else {
        Log.e(TAG, message, cause)
    }
}

/**
 * Throws an error or logs an exception based on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, throws an
 * [IllegalStateException] with the specified [message] and cause retrieved from [Result.exceptionOrNull].
 *
 * If only [BuildConfig.DEBUG] is set to `true`, logs [message] and the cause
 * retrieved from [Result.exceptionOrNull] at log level [android.util.Log.ERROR].
 *
 * If neither are set to `true` nothing happens.
 *
 * @param message The message describing the error
 * @param result The result which contains the cause of the error
 * */
fun error(message: String, result: Result<Any?>) {
    return error(message, result.exceptionOrNull())
}

/**
 * Throw an error, or execute a recovery function which returns a value, depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, an [IllegalStateException]
 * will be thrown containing the specified [message] and [cause], otherwise [recover] will be executed and
 * its return value will be returned from this function.
 *
 * The [message] and [cause] will be logged with log level [android.util.Log.ERROR] along with the execution
 * of [recover] if [BuildConfig.DEBUG] is set to `true`.
 *
 * @param message The message describing the error
 * @param cause The cause of the error if one exists
 * @param recover The recovery function, which takes two parameters: the [message] and the [cause]
 * */
fun <T> error(message: String, cause: Throwable? = null, recover: (String, Throwable?) -> T): T {
    return if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, cause)
    } else if (BuildConfig.DEBUG) {
        Log.e(TAG, message, cause)
        recover(message, cause)
    } else {
        recover(message, cause)
    }
}

/**
 * Throw an error or run a recovery block which returns a value, depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, an [IllegalStateException]
 * will be thrown containing the specified [message] and the exception contained in the [result] as the cause
 * if one exists, otherwise [recover] will be executed and its return value will be returned from this
 * function.
 *
 * An error log with accompany the execution of [recover] if [BuildConfig.DEBUG] is set to `true`.
 *
 * @param message The message describing the error
 * @param result The [Result] which an exception will be retrieved from using [Result.exceptionOrNull]
 * @param recover The recovery block to be executed
 * */
fun <T> error(message: String, result: Result<Any?>, recover: (String, Throwable?) -> T): T {
    return error(message, result.exceptionOrNull(), recover)
}