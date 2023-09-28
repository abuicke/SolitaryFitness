package com.gravitycode.simpletracker.workout_list.domain

import androidx.lifecycle.ViewModel
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WorkoutListViewModel @Inject constructor() : ViewModel() {

    private val workoutListState: StateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())

    fun doSomething(workout: Workout) {

    }
}
