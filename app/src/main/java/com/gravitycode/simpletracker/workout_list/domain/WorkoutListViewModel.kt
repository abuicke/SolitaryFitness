package com.gravitycode.simpletracker.workout_list.domain

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class WorkoutListViewModel : ViewModel() {

    private val workoutListState = MutableStateFlow(WorkoutListState())
}
