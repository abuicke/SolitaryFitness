package com.gravitycode.solitaryfitness.log_workout.presentation

import com.gravitycode.solitaryfitness.log_workout.util.Workout
import java.time.LocalDate

sealed class LogWorkoutEvent {

    data class DateSelected(val date: LocalDate) : LogWorkoutEvent()

    data class Increment(val workout: Workout, val quantity: Int) : LogWorkoutEvent()

    object Reset : LogWorkoutEvent()

    object Edit : LogWorkoutEvent()
}