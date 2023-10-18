package com.gravitycode.simpletracker.workout_list.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.launch
import java.time.LocalDate

@ActivityScope
class WorkoutListViewModelImpl constructor(
    private val workoutHistoryRepo: WorkoutHistoryRepo
) : ViewModel(), WorkoutListViewModel {

    companion object {
        private val TAG = WorkoutListViewModelImpl::class.java.simpleName
    }

    private val currentDate: LocalDate = LocalDate.now()

    private val _state = mutableStateOf(WorkoutListState(currentDate))//, neverEqualPolicy())
    override val state: State<WorkoutListState> = _state

    init {
        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory(currentDate).collect { workoutHistory ->
                _state.value = WorkoutListState(currentDate, workoutHistory.toMap())
            }
        }
    }

    override fun onEvent(event: WorkoutListEvent) {
        when (event) {
            is WorkoutListEvent.Increment -> incrementWorkout(event.workout, event.quantity)
        }
    }

    private fun updateListState(workout: Workout, quantity: Int) {
        _state.value = state.value.copy(workout, state.value[workout] + quantity)
    }

    /**
     * TODO: What should be done when the button is pressed many times over.
     *  Should I delay for 100ms before writing to file?
     * */
    private fun incrementWorkout(workout: Workout, quantity: Int) {
        updateListState(workout, quantity)

        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory(currentDate).collect { workoutHistory ->
                workoutHistory[workout] += quantity
                val result = workoutHistoryRepo.writeWorkoutHistory(workoutHistory)
                /**
                 * TODO: Need to show error Toast or Snackbar when this fails!!
                 * */
                if (result.isFailure) {
                    // TODO: Test
                    updateListState(workout, -quantity)
                    val throwable = result.exceptionOrNull()
                    Log.e(TAG, "Failed to write workout history to repository", throwable)
                }
            }
        }
    }
}
