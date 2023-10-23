package com.gravitycode.simpletracker.track_reps.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.track_reps.util.Workout
import com.gravitycode.simpletracker.util.ui.ToastDuration
import com.gravitycode.simpletracker.util.ui.Toaster
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@ActivityScope
class TrackRepsViewModel(
    private val toaster: Toaster,
    private val workoutHistoryRepo: WorkoutHistoryRepo,
) : ViewModel() {

    companion object {
        private val TAG = TrackRepsViewModel::class.java.simpleName
    }

    private var currentDate: LocalDate = LocalDate.now()

    private val _state = mutableStateOf(TrackRepsState(currentDate))
    val state: State<TrackRepsState> = _state

    init {
        loadWorkoutHistory()
    }

    fun onEvent(event: TrackRepsEvent) {
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

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        _state.value = state.value.copy(workout, state.value[workout] + quantity)

        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory(currentDate).collect { workoutHistory ->
                workoutHistory[workout] += quantity
                val result = workoutHistoryRepo.writeWorkoutHistory(currentDate, workoutHistory)
                if (result.isFailure) {
                    // TODO: Test
                    _state.value = state.value.copy(workout, state.value[workout] - quantity)
                    val errorMessage = "Failed to write workout history to repository"
                    toaster(errorMessage, ToastDuration.SHORT)
                    val throwable = result.exceptionOrNull()
                    Log.e(TAG, errorMessage, throwable)
                }
            }
        }
    }
}
