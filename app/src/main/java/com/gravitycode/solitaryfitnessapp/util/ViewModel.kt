package com.gravitycode.solitaryfitnessapp.util

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gravitycode.solitaryfitnessapp.app.AppEvent

/**
 * TODO: Move to caimito when ready.
 * */
abstract class ViewModel<S, E>(initialState: S) : androidx.lifecycle.ViewModel() {

    /**
     * TODO: So am I expected to use multiple states? Recomposing for any change in the view seems
     *  costly and not taking advantage of Compose's ability to to only recompose the relevant sections
     *
     * [State] is mutable and is often used to represent variables that can change within a Compose
     * composable function. Changes to a State trigger recomposition of the composable. - ChatGPT
     *
     * TODO: So should I use [mutableStateOf] more locally within composables to prevent recomposition of
     *  entire screen every time something updates?
     *
     * ### Lifecycle Awareness:
     *  + State is scoped to the composable function in which it is defined. It is automatically retained
     *  across recompositions, but its scope is limited to the current composable function execution.
     *  + LiveData is designed to be lifecycle-aware and is commonly used within ViewModel instances. It is
     *  specifically useful for handling changes in data over the lifecycle of an activity or fragment,
     *  preventing memory leaks and ensuring proper cleanup.
     * */
    private val _state = mutableStateOf(initialState)
    val state: State<S> = _state

    abstract fun onEvent(event: AppEvent)

    abstract fun onEvent(event: E)

    protected fun updateState(s: S) {
        _state.value = s
    }
}