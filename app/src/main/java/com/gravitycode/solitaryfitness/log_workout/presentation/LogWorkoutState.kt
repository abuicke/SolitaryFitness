package com.gravitycode.solitaryfitness.log_workout.presentation

import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import java.time.LocalDate

data class LogWorkoutState(
    val date: LocalDate = LocalDate.now(),
    val log: WorkoutLog = WorkoutLog(),
    val user: User? = null,
    val editMode: Boolean = false
)