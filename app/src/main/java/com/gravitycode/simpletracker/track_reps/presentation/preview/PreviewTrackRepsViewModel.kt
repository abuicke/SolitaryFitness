package com.gravitycode.simpletracker.track_reps.presentation.preview

import androidx.compose.runtime.State
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsEvent
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsState
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsViewModel
import java.time.LocalDate

class PreviewTrackRepsViewModel(
    date: LocalDate = LocalDate.now(),
    allReps: Int = 0,
    private val trackRepsState: TrackRepsState =
        TrackRepsState(
            date,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps,
            allReps
        )
) : TrackRepsViewModel {

    override val state = object : State<TrackRepsState> {
        override val value = trackRepsState
    }

    override fun onEvent(event: TrackRepsEvent) = Unit
}