package com.gravitycode.solitaryfitness.util.android

import com.gravitycode.solitaryfitness.BuildConfig

object Log {

    private const val META_TAG = "sf-app: "

    fun v(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v("$META_TAG$tag", message, throwable)
        }
    }

    fun d(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d("$META_TAG$tag", message, throwable)
        }
    }

    fun i(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i("$META_TAG$tag", message, throwable)
        }
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w("$META_TAG$tag", message, throwable)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e("$META_TAG$tag", message, throwable)
        }
    }

    fun wtf(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            android.util.Log.wtf("$META_TAG$tag", message, throwable)
        }
    }
}