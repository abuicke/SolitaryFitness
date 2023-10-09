package com.gravitycode.simpletracker.workout_list.presentation.preview

import androidx.compose.runtime.State
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListEvent
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListState
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListViewModel

class PreviewWorkoutListViewModel(
    private val workoutListState: WorkoutListState = WorkoutListState()
) : WorkoutListViewModel {

    override val state = object: State<WorkoutListState> {
        override val value = workoutListState
    }

    override fun onEvent(event: WorkoutListEvent) = Unit
}