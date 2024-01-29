package com.gravitycode.solitaryfitness.util.android

import com.gravitycode.solitaryfitness.BuildConfig

/**
 * Verbose: An event occurred. A piece of functionality (e.g. function, service, network call) has just
 * begun executing, or is in the process of performing some work but has not yet completed.
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
     * Log events which do not belong to any other log level.
     *
     * Used to create an overall picture of the app's functioning without needing to use other tools such as
     * a debugger. Useful in monitoring the consequences of changes to the overall functioning of the app and
     * for catching situation where events aren't occurring where expected, in wrong order or too many times.
     * */
    fun v(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v("$META_TAG$tag", message, throwable)
        }
    }

    /**
     * Log events which are crucial to the functioning of the application which have executed correctly.
     * */
    fun i(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i("$META_TAG$tag", message, throwable)
        }
    }

    /**
     * Log events which could be indicative of an error, but which have not actually raised an exception.
     * */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w("$META_TAG$tag", message, throwable)
        }
    }

    /**
     * Log events which have or could have raised an exception if not intercepted.
     * */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e("$META_TAG$tag", message, throwable)
        }
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
}