package com.gravitycode.simpletracker.util.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.gravitycode.simpletracker.app.ApplicationScope

/**
 * TODO: This should be implementing an interface for testing, but then I lose the nice, neat
 *  syntax of overriding the invoke operator. What's done in the case of use case testing?
 *  An interface plus a factory would solve this problem?
 *  Can also just mark the toaster parameter as nullable and not pass it in during testing.
 * */
@ApplicationScope
class Toaster(private val context: Context) {

    operator fun invoke(text: String, duration: ToastDuration) {
        Toast.makeText(context, text, duration.toInt()).show()
    }
}