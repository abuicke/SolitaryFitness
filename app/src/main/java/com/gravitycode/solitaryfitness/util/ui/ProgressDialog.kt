package com.gravitycode.solitaryfitness.util.ui

import android.app.AlertDialog
import android.content.Context
import android.util.Log

/**
 * TODO: I think I wanted to use this to display a progress dialog that stays on the screen for a minimum
 *  amount of time, [using Kotlin couroutines](https://github.com/Kotlin/kotlinx.coroutines/issues/1186).
 *
 *  Should be a modal dialog, i.e. not cancellable.
 * */
class ProgressDialog(context: Context) : AlertDialog(context) {

    override fun show() {
        super.show()

    }

    override fun dismiss() {
        super.dismiss()

    }
}