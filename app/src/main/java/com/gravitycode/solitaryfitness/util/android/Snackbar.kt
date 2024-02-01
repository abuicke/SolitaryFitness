package com.gravitycode.solitaryfitness.util.android

import androidx.compose.material3.SnackbarDuration

data class Snackbar(
    val message: String,
    val duration: SnackbarDuration,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val text: String,
    val onClick: () -> Unit
)