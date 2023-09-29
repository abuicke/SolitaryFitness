package com.gravitycode.simpletracker.workout_list.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO: I'm trying to determine if the Context dependency on DataStore is null
 * */
class WorkoutListViewModel @Inject constructor(
    private val workoutHistoryRepository: WorkoutHistoryRepository
) : ViewModel() {

    private val workoutListState: StateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())

    fun doSomething(workout: Workout) {
        Log.i("WorkoutListViewModel", workout.toString())

        viewModelScope.launch {
            workoutHistoryRepository.writeWorkoutHistory(WorkoutHistory(
                mapOf(
                    /** TODO: Can this be a partial map? */
                )
            ))
            workoutHistoryRepository.readWorkoutHistory()
        }
    }
}
