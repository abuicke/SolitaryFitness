package com.gravitycode.solitaryfitness.track_reps.presentation

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.time.LocalDate

class TrackRepsState(
    val date: LocalDate,
    val log: WorkoutLog = WorkoutLog()
)