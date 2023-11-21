package com.gravitycode.solitaryfitness.log_workout.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Toaster
import com.gravitycode.solitaryfitness.util.ui.ViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Never collect a flow from the UI directly from launch or the launchIn extension function if the UI needs
 * to be updated. These functions process events even when the view is not visible. This behavior can lead
 * to app crashes. To avoid that, use [androidx.lifecycle.repeatOnLifecycle].
 *
 * See warning in (StateFlow and SharedFlow)[https://developer.android.com/kotlin/flow/stateflow-and-sharedflow]
 * */
class LogWorkoutViewModel(
    private val appController: AppController,
    private val toaster: Toaster,
    private val repositoryFactory: WorkoutLogsRepositoryFactory
) : ViewModel<LogWorkoutState, LogWorkoutEvent>() {

    private companion object {

        const val TAG = "TrackRepsViewModel"
    }

    override val state = mutableStateOf(LogWorkoutState())


    /**
     *
     *
     * TODO: `readWorkoutLog(2023-11-20): null` is running 4 times in a row when signed in.
     *
     *
     * TODO: As far as I can remember I wanted to try going further with using generics to collapse the two
     *  onEvent functions into one.
     *
     *
     *
     *
     * */


    private lateinit var repository: WorkoutLogsRepository

    // Specifies whether a workout log already exists for
    // this date, in which case the update function should
    // be called on the repository. If a new record needs
    // to be created the write function should be used.
    private var doesRecordAlreadyExist = false

    init {
        /**
         * TODO: Need to make sure this job is canceled when I no longer need to observe this state.
         *  Check why and how the Job was cancelled in the clean architecture project.
         * */
        viewModelScope.launch {
            appController.appState.collect { appState ->
                Log.d(TAG, "app state collected: $appState")
                state.value = state.value.copy(user = appState.user)
                repository = repositoryFactory.create(appState.isUserSignedIn())
                loadWorkoutLog()
            }
        }
    }

    override fun onEvent(event: AppEvent) {
        when (event) {
            is AppEvent.SignIn -> appController.requestSignIn()
            is AppEvent.SignOut -> appController.requestSignOut()
        }
    }

    override fun onEvent(event: LogWorkoutEvent) {
        when (event) {
            is LogWorkoutEvent.DateSelected -> changeDate(event.date)
            is LogWorkoutEvent.Increment -> incrementWorkout(event.workout, event.quantity)
            is LogWorkoutEvent.Reset -> resetReps()
            is LogWorkoutEvent.Edit -> editReps()
        }
    }

    private suspend fun loadWorkoutLog() {
        val currentDate = state.value.date
        val result = repository.readWorkoutLog(currentDate)
        if (result.isSuccess) {
            val workoutLog = result.getOrNull()
            if (workoutLog != null) {
                doesRecordAlreadyExist = true
                state.value = state.value.copy(log = workoutLog)
            } else {
                doesRecordAlreadyExist = false
                state.value = state.value.copy(log = WorkoutLog())
            }
        } else {
            debugError("failed to read workout log from repository", result)
        }
    }

    private fun changeDate(date: LocalDate) {
        if(date != state.value.date) {
            state.value = state.value.copy(date = date)
            viewModelScope.launch {
                loadWorkoutLog()
            }
        }
    }

    /**
     * TODO: Could just retain a reference to the old TrackRepsState and reassign that if the update fails
     *  This logic is implemented, need to test by setting `if(result.isFailure)` to `if(!result.isFailure)`
     * */
    private fun incrementWorkout(workout: Workout, quantity: Int) {
        require(quantity >= 0) { "cannot increment by a negative value" }

        val oldState = state.value
        val currentDate = state.value.date
        val newReps = state.value.log[workout] + quantity
        state.value = state.value.copy(log = state.value.log.copy(workout, newReps))

        viewModelScope.launch {
            /**
             * TODO: Shouldn't this initially be set to `!doesRecordAlreadyExist`
             * */
            var firstTimeWrite = false
            val result = if (doesRecordAlreadyExist) {
                repository.updateWorkoutLog(currentDate, workout, newReps)
            } else {
                firstTimeWrite = true
                doesRecordAlreadyExist = true
                repository.writeWorkoutLog(currentDate, state.value.log)
            }
            if (result.isFailure) {
                doesRecordAlreadyExist = !firstTimeWrite && doesRecordAlreadyExist
                state.value = oldState
                toaster("Couldn't save reps")
                debugError("Failed to write workout history to repository", result)
            } else {
                Log.v(TAG, "incrementWorkout(${workout.string}, $quantity)")
            }
        }
    }

    private fun resetReps() {
        toaster("Reset Reps")
//        _state.value = LogWorkoutState(currentDate, WorkoutLog())
//        viewModelScope.launch {
//            repository.writeWorkoutLog(currentDate, state.value.log)
//        }
    }

    private fun editReps() {
        toaster("Edit Reps")
    }
}
