package com.gravitycode.simpletracker.workout_list.domain

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

    /**
     * TODO: Should this be read from a [androidx.compose.runtime.State] variable? They seem custom
     * built to work with composables but I'm still not sure exactly in what way.
     *
     * Are there variables like [kotlinx.coroutines.flow.StateFlow] which will automatically
     * render updates to the composable? That would be the ideal situation.
     * */
    fun listItems() = Workout.values()

    fun onEvent(event: WorkoutListEvent) {
        when (event) {
            is WorkoutListEvent.Increment -> incrementWorkout(event.workout, event.quantity)
        }
    }

    private fun incrementWorkout(workout: Workout, quantity: Int) {
        viewModelScope.launch {
            workoutHistoryRepo.readWorkoutHistory().collect { workoutHistory ->
                workoutHistory[workout] = workoutHistory[workout] + quantity
                workoutHistoryRepo.writeWorkoutHistory(workoutHistory)
            }
        }
    }
}
