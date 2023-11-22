package com.gravitycode.solitaryfitness.util.ui

import androidx.compose.runtime.State
import com.gravitycode.solitaryfitness.app.AppEvent

abstract class ViewModel<S, E> : androidx.lifecycle.ViewModel() {

    abstract val state: State<S>

    abstract fun onEvent(event: AppEvent)

    abstract fun onEvent(event: E)
}