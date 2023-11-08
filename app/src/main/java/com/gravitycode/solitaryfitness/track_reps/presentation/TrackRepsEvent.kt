package com.gravitycode.solitaryfitness.track_reps.presentation

import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.time.LocalDate

sealed class TrackRepsEvent: AppEvent<TrackRepsEvent>() {

    data class DateSelected(val date: LocalDate) : TrackRepsEvent()
    data class Increment(val workout: Workout, val quantity: Int) : TrackRepsEvent()
}