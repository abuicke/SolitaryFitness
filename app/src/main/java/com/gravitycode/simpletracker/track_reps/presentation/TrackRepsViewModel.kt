package com.gravitycode.simpletracker.track_reps.presentation

import androidx.compose.runtime.State

interface TrackRepsViewModel {

    val state: State<TrackRepsState>

    fun onEvent(event: TrackRepsEvent)
}