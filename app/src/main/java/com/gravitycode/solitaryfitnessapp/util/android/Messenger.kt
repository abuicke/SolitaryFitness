package com.gravitycode.solitaryfitnessapp.util.android

import androidx.compose.material3.SnackbarDuration

interface Messenger {

    fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT)

    fun showSnackbar(snackbar: Snackbar)

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short)
}