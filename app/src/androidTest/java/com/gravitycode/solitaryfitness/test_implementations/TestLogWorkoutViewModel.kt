package com.gravitycode.solitaryfitness.test_implementations

import androidx.compose.runtime.mutableStateOf
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutEvent
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutState
import com.gravitycode.solitaryfitness.util.ui.ViewModel

class TestLogWorkoutViewModel : ViewModel<LogWorkoutState, LogWorkoutEvent>() {

    override val state = mutableStateOf(LogWorkoutState())

    override fun onEvent(event: AppEvent) {

    }

    override fun onEvent(event: LogWorkoutEvent) {

    }
}