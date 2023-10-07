package com.gravitycode.simpletracker.workout_list.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel should work like this:
 *      1) ViewModel sends View what figures to display.
 *      2) When a workout it clicked, notify the ViewModel it has been clicked.
 *      3) ViewModel updates [WorkoutHistoryRepository]
 *      4) ViewModel sends new value to the View to be rendered for the workout.
 *
 * View should be agnostic of any calculations going on. It should not increment the figures in the
 * View by its own logic. It should simply dispatch that a click happened, and let the ViewModel
 * calculate what the View should now display.
 *
 * It makes sense then to use an observable state in thr ViewModel that the View subscribes to so it
 * can update to reflect the changes made in the ViewModel.
 *
 * The View should only ever accept user input and dispatch it to the ViewModel. The ViewModel will
 * do the necessary calculations and send back the new state the View should render. There should be
 * no logic in the View, no matter how simple.
 * */
@ActivityScope
class WorkoutListViewModel @Inject constructor(
    private val workoutHistoryRepo: WorkoutHistoryRepo
) : ViewModel() {

    private val _state = mutableStateOf(WorkoutListState())
    val state: State<WorkoutListState> = _state

    init {
        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory().collect { workoutHistory ->
                _state.value = WorkoutListState.from(workoutHistory)
            }
        }

        mutableStateListOf()
    }

    fun onEvent(event: WorkoutListEvent) {
        when (event) {
            is WorkoutListEvent.Increment -> incrementWorkout(event.workout, event.quantity)
        }
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {

        val workoutListState = _state.value.copy()
        /**
         * TODO: Replace with [WorkoutListState.inc]
         * */
        workoutListState[workout] = workoutListState[workout] + quantity
        _state.value = workoutListState

        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory().collect { workoutHistory ->
                workoutHistory[workout] = workoutHistory[workout] + quantity
                workoutHistoryRepo.writeWorkoutHistory(workoutHistory)

//                _state.value = state.value.copy(
                /**
                 *
                 *
                 * TODO: I'm not here at all. Hopefully this will make sense tomorrow.
                 * The point of this though is that the new state should get propagated
                 * back the View and rendered after writing to the [WorkoutHistory]
                 * completes. There should be a way of knowing weather writeWorkoutHistory(WorkoutHistory)
                 * has succeeded or not before assigning the new value. Actually, the new
                 * value should be set immediately, and then reduced back down if the write
                 * operation fails (which I need a way of detecting).
                 *
                 *
                 *
                 * */
//                )
            }
        }
    }
}
