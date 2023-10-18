package com.gravitycode.simpletracker.workout_list.presentation

import com.gravitycode.simpletracker.workout_list.util.Workout
import java.time.LocalDate

sealed class WorkoutListEvent {

    class DateSelected(val date: LocalDate) : WorkoutListEvent()
    class Increment(val workout: Workout, val quantity: Int) : WorkoutListEvent()
}