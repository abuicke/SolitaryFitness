package com.gravitycode.simpletracker.workout_list.presentation

import androidx.compose.runtime.State

interface WorkoutListViewModel {

    val state: State<WorkoutListState>

    fun onEvent(event: WorkoutListEvent)
}