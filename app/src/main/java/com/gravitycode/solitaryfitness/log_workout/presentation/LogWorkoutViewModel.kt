package com.gravitycode.solitaryfitness.log_workout.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Messenger
import com.gravitycode.solitaryfitness.util.ui.ViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Never collect a flow from the UI directly from launch or the launchIn extension function if the UI needs
 * to be updated. These functions process events even when the view is not visible. This behavior can lead
 * to app crashes. To avoid that, use [androidx.lifecycle.repeatOnLifecycle].
 *
 * See warning in (StateFlow and SharedFlow)[https://developer.android.com/kotlin/flow/stateflow-and-sharedflow]
 *
 * TODO: Do I need to implement this behavior?
 * */
class LogWorkoutViewModel(
    private val appController: AppController,
    private val messenger: Messenger,
    private val repositoryFactory: WorkoutLogsRepositoryFactory
) : ViewModel<LogWorkoutState, LogWorkoutEvent>() {

    private companion object {

        const val TAG = "TrackRepsViewModel"
    }

    private val _state = mutableStateOf(LogWorkoutState())
    override val state: State<LogWorkoutState> = _state

    private lateinit var repository: WorkoutLogsRepository

    init {
        /**
         * TODO: Need to make sure this job is canceled when I no longer need to observe this state.
         *  This would probably be resolved by simply using a ViewModel factory in MainActivity instead of
         *  injecting the ViewModel directly and using the same pattern I have right here with the repo.
         * */
        viewModelScope.launch {
            appController.appState.collect { appState ->
                Log.d(TAG, "app state collected: $appState")
                _state.value = _state.value.copy(user = appState.user)
                repository = if (appState.isUserSignedIn()) {
                    repositoryFactory.getOnlineRepository()
                }else {
                    repositoryFactory.getOfflineRepository()
                }
                loadWorkoutLog()
            }
        }
    }

    override fun onEvent(event: AppEvent) {
        Log.v(TAG, "onEvent($event)")
        when (event) {
            is AppEvent.SignIn -> appController.requestSignIn()
            is AppEvent.SignOut -> appController.requestSignOut()
        }
    }

    override fun onEvent(event: LogWorkoutEvent) {
        Log.v(TAG, "onEvent($event)")
        when (event) {
            is LogWorkoutEvent.DateSelected -> changeDate(event.date)
            is LogWorkoutEvent.Increment -> incrementWorkout(event.workout, event.quantity)
            is LogWorkoutEvent.Reset -> resetReps()
            is LogWorkoutEvent.Edit -> editReps(event.mode)
        }
    }

    /**
     * This function exists until I decide if there's a better way of letting the view model know it needs
     * to read from the repository again. It's not clear who should have this responsibility, so for now I'm
     * deciding that the MainActivity will coordinate this as it requires the minimum amount of code to
     * implement, and no changes to any other classes bar adding this rudimentary method here.
     *
     * TODO: If I decide this is the appropriate to do this then I should add [invalidate] to [ViewModel]
     * */
    fun invalidate() {

    }

    private suspend fun loadWorkoutLog() {
        val currentDate = _state.value.date
        val result = repository.readWorkoutLog(currentDate)

        if (result.isSuccess) {
            val workoutLog = result.getOrNull()
            if (workoutLog != null) {
                _state.value = _state.value.copy(log = workoutLog)
            } else {
                _state.value = _state.value.copy(log = WorkoutLog())
            }
        } else {
            debugError("failed to read workout log from repository", result)
        }
    }

    private fun changeDate(date: LocalDate) {
        if (date != _state.value.date) {
            _state.value = _state.value.copy(date = date)
            viewModelScope.launch {
                loadWorkoutLog()
            }
        }
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        require(quantity >= 0) { "cannot increment by a negative value" }

        val oldState = _state.value
        val currentDate = _state.value.date
        val newReps = _state.value.log[workout] + quantity
        _state.value = _state.value.copy(log = _state.value.log.copy(workout, newReps))

        viewModelScope.launch {
            val recordAlreadyExists = repository.metaData.containsRecord(currentDate)
            val result = if (recordAlreadyExists) {
                repository.updateWorkoutLog(currentDate, workout, newReps)
            } else {
                val log = WorkoutLog.from(
                    hashMapOf(
                        workout to newReps
                    )
                )
                repository.writeWorkoutLog(currentDate, log)
            }

            if (result.isFailure) {
                _state.value = oldState
                messenger.toast("Couldn't save reps")
                debugError("Failed to write workout log to repository", result)
            } else {
                Log.v(TAG, "incrementWorkout(${workout.string}, $quantity)")
            }
        }
    }

    private fun resetReps() {
        val oldState = _state.value

        val log = WorkoutLog()
        _state.value = _state.value.copy(log = log)

        viewModelScope.launch {
            val result = repository.writeWorkoutLog(_state.value.date, log)
            if (result.isFailure) {
                _state.value = oldState
                messenger.toast("Couldn't reset reps")
                debugError("Failed to reset reps and write to repository", result)
            } else {
                Log.v(TAG, "reps reset successfully")
            }
        }
    }

    /**
     * TODO: When in edit mode turn each Text that contains a rep count into an EditText and show two
     *  floating action buttons, one to save the changes that were made (floppy disk save icon - pull save
     *  icon from clean architecture notes app) and one to cancel all the edits that were made (X icon
     *  [https://www.google.com/search?q=x+close+icon])
     * */
    private fun editReps(mode: LogWorkoutEvent.Edit.Mode) {
        _state.value = _state.value.copy(editMode = mode == LogWorkoutEvent.Edit.Mode.START)
    }
}
