package dev.gravitycode.solitaryfitness.logworkout.presentation

import dev.gravitycode.solitaryfitness.logworkout.domain.Workout
import java.time.LocalDate

sealed class LogWorkoutEvent {

    data class DateSelected(val date: LocalDate) : LogWorkoutEvent()

    data class Increment(val workout: Workout, val quantity: Int) : LogWorkoutEvent()

    object Reset : LogWorkoutEvent() {
        override fun toString() = "LogWorkoutEvent.Reset"
    }

    data class Edit(val mode: Mode) : LogWorkoutEvent() {
        enum class Mode {
            START, SAVE, CANCEL
        }
    }
}