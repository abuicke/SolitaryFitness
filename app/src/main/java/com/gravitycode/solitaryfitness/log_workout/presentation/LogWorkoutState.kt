package com.gravitycode.solitaryfitness.log_workout.presentation

import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import java.time.LocalDate

class LogWorkoutState(
    val date: LocalDate,
    val log: WorkoutLog = WorkoutLog(),
    val editMode: Boolean = false
)