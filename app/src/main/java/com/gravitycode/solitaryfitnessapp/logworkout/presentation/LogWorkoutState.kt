package com.gravitycode.solitaryfitnessapp.logworkout.presentation

import com.gravitycode.solitaryfitnessapp.auth.User
import com.gravitycode.solitaryfitnessapp.logworkout.domain.WorkoutLog
import java.time.LocalDate

data class LogWorkoutState(
    val date: LocalDate = LocalDate.now(),
    val log: WorkoutLog = WorkoutLog(),
    val user: User? = null,
    val editMode: Boolean = false,
)