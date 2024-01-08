package com.gravitycode.solitaryfitness.util.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.UiThread

interface Messenger {

    companion object {

        fun create(context: Context): Messenger = MessengerImpl(context)
    }

    @UiThread
    fun toast(text: String, duration: ToastDuration = ToastDuration.SHORT)

    /**
     * TODO: Verify that this also needs to be called from the UI Thread or if it's implementation varies.
     * */
    @UiThread
    fun snackbar(text: String, action: String)
}

private class MessengerImpl(private val context: Context): Messenger {

    override fun toast(text: String, duration: ToastDuration) {
        Toast.makeText(context, text, duration.toInt()).show()
    }

    /**
     * TODO: Probably need to put some kind of a SnackbarHost interface on the MainActivity and expose that
     *  privately in Dagger to pass to the Messenger provides function.
     * */
    override fun snackbar(text: String, action: String) {
        TODO("Not yet implemented")
    }
}