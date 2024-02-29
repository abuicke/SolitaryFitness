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
 * If only [BuildConfig.DEBUG] is set to `true`, logs [message] and [cause] at level [android.util.Log.ERROR].
 *
 * If neither are set to `true` nothing happens.
 *
 * @param message The message describing the error
 * @param cause The cause of the error if one exists
 * */
fun error(message: String, cause: Throwable? = null) {
    if (BuildConfig.DEBUG && BuildConfig.CRASH_ON_ERROR) {
        throw IllegalStateException(message, cause)
    } else if (BuildConfig.DEBUG) {
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
 * retrieved from [Result.exceptionOrNull] at level [android.util.Log.ERROR].
 *
 * If neither are set to `true` nothing happens.
 *
 * @param message The message describing the error
 * @param result The result which [Result.exceptionOrNull] will be called on to retrieve the cause if one exists
 * */
fun error(message: String, result: Result<*>) {
    error(message, result.exceptionOrNull())
}

/**
 * Throws an error or executes a recovery function depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`,
 * throws an [IllegalStateException] with the specified [message] and [cause].
 *
 * If only [BuildConfig.DEBUG] is set to `true`, logs [message] and [cause] at
 * level [android.util.Log.ERROR] and executes [recover] returning its return value.
 *
 * If neither are set to `true`, executes [recover] and returns its return value.
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
 * Throws an error or executes a recovery function depending on the build configuration.
 *
 * If both [BuildConfig.DEBUG] and [BuildConfig.CRASH_ON_ERROR] are set to `true`, throws an
 * [IllegalStateException] with the specified [message] and cause retrieved from [Result.exceptionOrNull]
 *
 * If only [BuildConfig.DEBUG] is set to `true`, logs [message] and the cause retrieved from
 * [Result.exceptionOrNull] at level [android.util.Log.ERROR] and executes [recover] returning its return value.
 *
 * If neither are set to `true`, executes [recover] and returns its return value.
 *
 * @param message The message describing the error
 * @param result The result which [Result.exceptionOrNull] will be called on to retrieve the cause if one exists
 * @param recover The recovery function, which takes two parameters: the [message] and the cause retrieved from [Result.exceptionOrNull]
 * */
fun <T> error(message: String, result: Result<*>, recover: (String, Throwable?) -> T): T {
    return error(message, result.exceptionOrNull(), recover)
}