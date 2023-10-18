package com.gravitycode.simpletracker.workout_list.presentation.preview

import androidx.compose.runtime.State
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListEvent
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListState
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListViewModel
import java.time.LocalDate

class PreviewWorkoutListViewModel(
    date: LocalDate = LocalDate.now(),
    allReps: Int = 0,
    private val workoutListState: WorkoutListState =
        WorkoutListState(
            date,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps
        )
) : WorkoutListViewModel {

    override val state = object : State<WorkoutListState> {
        override val value = workoutListState
    }

    override fun onEvent(event: WorkoutListEvent) = Unit
}