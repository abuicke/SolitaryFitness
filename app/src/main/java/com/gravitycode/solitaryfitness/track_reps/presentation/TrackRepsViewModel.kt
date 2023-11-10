package com.gravitycode.solitaryfitness.track_reps.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.solitaryfitness.app.ActivityScope
import com.gravitycode.solitaryfitness.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.ToastDuration
import com.gravitycode.solitaryfitness.util.ui.Toaster
import kotlinx.coroutines.launch
import java.time.LocalDate

@ActivityScope
class TrackRepsViewModel(
    private val toaster: Toaster,
    private val workoutHistoryRepo: WorkoutHistoryRepo
) : ViewModel() {

    companion object {
        const val TAG = "TrackRepsViewModel"
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
            val result = workoutHistoryRepo.readWorkoutLog(currentDate)
            if (result.isSuccess) {
                val workoutLog = result.getOrNull()!!
                _state.value = TrackRepsState(currentDate, workoutLog.toMap())
            } else {
                debugError("failed to read workout log from repository", result)
            }
        }
    }

    private fun changeDate(date: LocalDate) {
        if (currentDate != date) {
            loadWorkoutHistory()
        }
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        val newReps = state.value[workout] + quantity
        _state.value = state.value.copy(workout, newReps)

        viewModelScope.launch {
            val result = workoutHistoryRepo.updateWorkoutLog(currentDate, workout, newReps)
            if (result.isSuccess) {
                _state.value = state.value.copy(workout, state.value[workout] - quantity)
                toaster("Couldn't save reps", ToastDuration.SHORT)
                debugError("Failed to write workout history to repository", result)
            } else {
                Log.v(TAG, "incrementWorkout(${workout.string}, $quantity)")
            }
        }
    }
}
