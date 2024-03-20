package dev.gravitycode.solitaryfitness.logworkout.presentation

import androidx.lifecycle.viewModelScope
import dev.gravitycode.caimito.kotlin.core.Log
import dev.gravitycode.caimito.kotlin.core.error
import dev.gravitycode.caimito.kotlin.ui.Messenger
import dev.gravitycode.solitaryfitness.app.AppEvent
import dev.gravitycode.solitaryfitness.app.FlowLauncher
import dev.gravitycode.solitaryfitness.auth.AuthenticationObservable
import dev.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepositoryFactory
import dev.gravitycode.solitaryfitness.logworkout.domain.Workout
import dev.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import dev.gravitycode.solitaryfitness.util.ViewModel
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
 *
 * ViewModel classes should prefer creating coroutines instead of exposing suspend functions to perform
 * business logic. Suspend functions in the ViewModel can be useful if instead of exposing state using a
 * stream of data, only a single value needs to be emitted.
 *
 * In addition to that, your coroutines will survive configuration changes automatically if the work is
 * started in the viewModelScope. If you create coroutines using lifecycleScope instead, you'd have to
 * handle that manually. If the coroutine needs to outlive the ViewModel's scope, check out the Creating
 * coroutines in the business and data layer section.
 *
 * [https://developer.android.com/kotlin/coroutines/coroutines-best-practices#viewmodel-coroutines]
 * */
class LogWorkoutViewModel(
    private val messenger: Messenger,
    private val authenticationObservable: AuthenticationObservable,
    private val flowLauncher: FlowLauncher,
    private val repositoryFactory: WorkoutLogsRepositoryFactory
) : ViewModel<LogWorkoutState, LogWorkoutEvent>(LogWorkoutState()) {

    private companion object {

        const val TAG = "TrackRepsViewModel"
    }

    private lateinit var repository: WorkoutLogsRepository

    init {
        viewModelScope.launch {
            authenticationObservable.authState.collect { appState ->
                Log.i(TAG, "app state collected: $appState")
                updateState(state.value.copy(user = appState.user))
                repository = if (appState.isUserSignedIn()) {
                    repositoryFactory.getOnlineRepository()
                } else {
                    repositoryFactory.getOfflineRepository()
                }
                loadWorkoutLog()
            }
        }
    }

    override fun onEvent(event: AppEvent) {
        Log.v(TAG, "onEvent($event)")
        when (event) {
            is AppEvent.SignIn -> flowLauncher.launchSignInFlow()
            is AppEvent.SignOut -> flowLauncher.launchSignOutFlow()
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

    override fun onCleared() {
        Log.v(TAG, "$this cleared")
    }

    private suspend fun loadWorkoutLog() {
        val currentDate = state.value.date
        val result = repository.readWorkoutLog(currentDate)

        if (result.isSuccess) {
            val workoutLog = result.getOrNull()
            if (workoutLog != null) {
                updateState(state.value.copy(log = workoutLog))
            } else {
                updateState(state.value.copy(log = WorkoutLog()))
            }
        } else {
            error("failed to read workout log from repository", result) {
                messenger.showToast("Unable to load workout data")
            }
        }
    }

    private fun changeDate(date: LocalDate) {
        if (date != state.value.date) {
            updateState(state.value.copy(date = date))
            viewModelScope.launch {
                loadWorkoutLog()
            }
        }
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        require(quantity >= 0) { "cannot increment by a negative value" }

        val oldState = state.value
        val currentDate = state.value.date
        val newReps = state.value.log[workout] + quantity
        val updatedWorkoutLog = state.value.log.copy(workout, newReps)
        updateState(state.value.copy(log = updatedWorkoutLog))

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
                error("Failed to write workout log to repository", result) {
                    updateState(oldState)
                    messenger.showToast("Couldn't save reps")
                }
            } else {
                Log.i(TAG, "incrementWorkout(${workout.string}, $quantity)")
            }
        }
    }

    private fun resetReps() {
        val oldState = state.value

        val log = WorkoutLog()
        updateState(state.value.copy(log = log))

        viewModelScope.launch {
            val result = repository.writeWorkoutLog(state.value.date, log)
            if (result.isFailure) {
                error("Failed to reset reps and write to repository", result) {
                    updateState(oldState)
                    messenger.showToast("Couldn't reset reps")
                }
            } else {
                Log.i(TAG, "reps reset successfully")
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
        updateState(state.value.copy(editMode = mode == LogWorkoutEvent.Edit.Mode.START))
    }
}
