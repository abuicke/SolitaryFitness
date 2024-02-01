package com.gravitycode.solitaryfitness.util

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.util.android.Snackbar

abstract class ViewModel<S : ViewState, E>(initialState: S) : androidx.lifecycle.ViewModel() {

    private val _state = mutableStateOf(initialState)
    val state: State<S> = _state

    abstract fun onEvent(event: AppEvent)

    abstract fun onEvent(event: E)

    abstract fun showSnackbar(snackbar: Snackbar)

    protected fun updateState(s: S) {
        _state.value = s
    }
}