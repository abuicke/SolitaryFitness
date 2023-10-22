package com.gravitycode.simpletracker.track_reps.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.track_reps.util.Workout
import kotlinx.coroutines.launch
import java.time.LocalDate

@ActivityScope
class TrackRepsViewModelImpl constructor(
    private val workoutHistoryRepo: WorkoutHistoryRepo
) : ViewModel(), TrackRepsViewModel {

    companion object {
        private val TAG = TrackRepsViewModelImpl::class.java.simpleName
    }

    private var currentDate: LocalDate = LocalDate.now()

    private val _state = mutableStateOf(TrackRepsState(currentDate))//, neverEqualPolicy())
    override val state: State<TrackRepsState> = _state

    init {
        loadWorkoutHistory()
    }

    override fun onEvent(event: TrackRepsEvent) {
        when (event) {
            is TrackRepsEvent.DateSelected -> changeDate(event.date)
            is TrackRepsEvent.Increment -> incrementWorkout(event.workout, event.quantity)
        }
    }

    private fun loadWorkoutHistory() {
        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory(currentDate).collect { workoutHistory ->
                _state.value = TrackRepsState(currentDate, workoutHistory.toMap())
            }
        }
    }

    private fun changeDate(date: LocalDate) {
        currentDate = date
        loadWorkoutHistory()
    }

    /**
     * TODO: What should be done when the button is pressed many times over.
     *  Should I delay for 100ms before writing to file?
     * */
    private fun incrementWorkout(workout: Workout, quantity: Int) {
        _state.value = state.value.copy(workout, state.value[workout] + quantity)

        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory(currentDate).collect { workoutHistory ->
                workoutHistory[workout] += quantity
                val result = workoutHistoryRepo.writeWorkoutHistory(currentDate, workoutHistory)
                /**
                 * TODO: Need to show error Toast or Snackbar when this fails!!
                 * */
                if (result.isFailure) {
                    // TODO: Test
                    _state.value = state.value.copy(workout, state.value[workout] - quantity)
                    val throwable = result.exceptionOrNull()
                    Log.e(TAG, "Failed to write workout history to repository", throwable)
                }
            }
        }
    }
}
