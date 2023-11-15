package com.gravitycode.solitaryfitness.app.ui

import android.widget.Toast

enum class ToastDuration(private val int: Int) {

    SHORT(Toast.LENGTH_SHORT),

    LONG(Toast.LENGTH_LONG);

    fun toInt() = int
}