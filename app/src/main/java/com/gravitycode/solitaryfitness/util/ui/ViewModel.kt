package com.gravitycode.solitaryfitness.util.ui

import androidx.compose.runtime.State

abstract class ViewModel<S, E> : androidx.lifecycle.ViewModel() {

    abstract val state: State<S>

    abstract fun onEvent(event: E)
}