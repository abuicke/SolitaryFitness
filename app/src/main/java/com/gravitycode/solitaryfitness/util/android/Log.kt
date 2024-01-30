package com.gravitycode.solitaryfitness.util.android

import com.gravitycode.solitaryfitness.BuildConfig

/**
 * Verbose: A non-critical event has succeeded. Also covers any event which does not belong at another level,
 * such as intermediate events (events which occur while a procedure is running which has not completed or
 * failed yet), or the initial execution of a procedure such as the beginning of a function call.
 *
 * Debug: A non-critical event has failed, e.g. a condition which is expected to not be met at times has not been.
 *
 * Info: An app critical event (one which will effect the user experience if it fails) has completed successfully.
 *
 * Warn: An app critical event has completed, but under conditions which may signal the presence of an
 * unrecognised error or bug. An exception may be raised down the line as the result of this unrecognised issue.
 *
 * Error: An app critical event has failed. An exception has been raised which must be recovered from gracefully.
 * */
object Log {

    private const val META_TAG = "sf-app: "

    /**
     * Log events which do not belong to any other log level; non-critical conditions succeeding, the state
     * of a particular procedure or module across time, lifecycle events, initial function calls etc.
     * */
    fun v(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun v(tag: String, message: () -> String) {
        v(tag, message())
    }

    /**
     * Log events in which non-critical conditions fail, e.g. an event was not triggered due to a certain
     * condition not being met.
     * */
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun d(tag: String, message: () -> String) {
        d(tag, message())
    }

    /**
     * Log events which are crucial to the functioning of the application which have executed correctly.
     * */
    fun i(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun i(tag: String, message: () -> String) {
        i(tag, message())
    }

    /**
     * Log events which could be indicative of an error, but which have not actually raised an exception.
     * */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun w(tag: String, message: () -> String) {
        w(tag, message())
    }

    /**
     * Log events which have or could have raised an exception if not intercepted.
     * */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun e(tag: String, message: () -> String) {
        e(tag, message())
    }

    /**
     * Log system critical events which have not occurred as the result of a bug in code, but as a result of
     * a failure of the framework, the virtual machine, or some other external tool or dependency.
     * */
    fun wtf(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.wtf("$META_TAG$tag", message, throwable)
        }
    }

    // message: () -> String allows for better formatting of long log messages in code. Not sure if there's
    // another reason Kotlin uses this pattern in it's standard libraries, but it's useful for this purpose.
    fun wtf(tag: String, message: () -> String) {
        wtf(tag, message())
    }
}