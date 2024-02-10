package com.gravitycode.solitaryfitness.util.android

interface Messenger {

    fun showToast(text: String, duration: ToastDuration = ToastDuration.SHORT)

    fun showSnackbar(snackbar: Snackbar)
}