package com.gravitycode.solitaryfitnessapp.test_implementations

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.gravitycode.solitaryfitnessapp.app.AppEvent
import com.gravitycode.solitaryfitnessapp.logworkout.presentation.LogWorkoutEvent
import com.gravitycode.solitaryfitnessapp.logworkout.presentation.LogWorkoutState
import com.gravitycode.solitaryfitnessapp.util.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain

/**
 * Note: The [viewModelScope] property of ViewModel classes is hardcoded to [Dispatchers.Main].
 * Replace it in tests by calling [Dispatchers.setMain] and passing in a test dispatcher.
 *
 * [https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers]
 * */
class TestLogWorkoutViewModel : ViewModel<LogWorkoutState, LogWorkoutEvent>() {

    override val state = mutableStateOf(LogWorkoutState())

    override fun onEvent(event: AppEvent) {

    }

    override fun onEvent(event: LogWorkoutEvent) {

    }
}