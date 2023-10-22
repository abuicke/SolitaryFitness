package com.gravitycode.simpletracker.track_reps.presentation

import com.gravitycode.simpletracker.track_reps.util.Workout
import java.time.LocalDate

sealed class TrackRepsEvent {

    class DateSelected(val date: LocalDate) : TrackRepsEvent()
    class Increment(val workout: Workout, val quantity: Int) : TrackRepsEvent()
}