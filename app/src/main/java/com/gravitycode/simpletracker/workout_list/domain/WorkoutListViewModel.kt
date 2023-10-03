package com.gravitycode.simpletracker.workout_list.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class WorkoutListViewModel @Inject constructor(
    private val workoutHistoryRepo: WorkoutHistoryRepo
) : ViewModel() {

    private val workoutListState: StateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())

    fun doSomething(workout: Workout) {
        Log.i("WorkoutListViewModel", workout.toString())

        viewModelScope.launch {
            workoutHistoryRepo.writeWorkoutHistory(
                WorkoutHistory(
                    mapOf(
                        Workout.PRESS_UP to 15
                    )
                )
            )
            workoutHistoryRepo.readWorkoutHistory().collect { workoutHistory ->
                Log.i("workout_debugging", workoutHistory.toString())
            }
        }
    }
}
