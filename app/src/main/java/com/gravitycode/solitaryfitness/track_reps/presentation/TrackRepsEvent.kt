package com.gravitycode.solitaryfitness.track_reps.presentation

import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.time.LocalDate

sealed class TrackRepsEvent {

    data class DateSelected(val date: LocalDate) : TrackRepsEvent()
    data class Increment(val workout: Workout, val quantity: Int) : TrackRepsEvent()
}