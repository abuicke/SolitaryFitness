package com.gravitycode.solitaryfitness.track_reps.presentation

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
        currentDate = date
        loadWorkoutHistory()
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        _state.value = state.value.copy(workout, state.value[workout] + quantity)

        viewModelScope.launch {
            val result = workoutHistoryRepo.readWorkoutLog(currentDate)
            if(result.isSuccess) {
                val workoutLog = result.getOrNull()!!
                workoutLog[workout] += quantity
                val result2 = workoutHistoryRepo.writeWorkoutLog(currentDate, workoutLog)
                if (result2.isFailure) {
                    _state.value = state.value.copy(workout, state.value[workout] - quantity)
                    val errorMessage = "Failed to write workout history to repository"
                    toaster(errorMessage, ToastDuration.SHORT)
                    debugError(errorMessage, result2)
                }
            }else {
                debugError("failed to read workout history repository", result)
            }
        }
    }
}
