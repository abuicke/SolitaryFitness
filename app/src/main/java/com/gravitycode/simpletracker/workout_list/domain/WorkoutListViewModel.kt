package com.gravitycode.simpletracker.workout_list.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class WorkoutListViewModel @Inject constructor(
    private val workoutHistoryRepository: WorkoutHistoryRepository
) : ViewModel() {

    private val workoutListState: StateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())

    fun doSomething(workout: Workout) {
        Log.i("WorkoutListViewModel", workout.toString())

        viewModelScope.launch {
            workoutHistoryRepository.writeWorkoutHistory(
                WorkoutHistory(
                    mapOf(
                        Workout.PRESS_UP to 15
                    )
                )
            )
            workoutHistoryRepository.readWorkoutHistory().collect { workoutHistory ->
                Log.i("workout_debugging", workoutHistory.toString())
            }
        }
    }
}
