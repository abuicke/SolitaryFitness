package com.gravitycode.solitaryfitnessapp.util.android

import android.widget.Toast

enum class ToastDuration(private val int: Int) {

    SHORT(Toast.LENGTH_SHORT),

    LONG(Toast.LENGTH_LONG);

    fun toInt() = int
}