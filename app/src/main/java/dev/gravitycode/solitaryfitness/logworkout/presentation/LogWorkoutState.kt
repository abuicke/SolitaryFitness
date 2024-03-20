package dev.gravitycode.solitaryfitness.logworkout.presentation

import dev.gravitycode.solitaryfitness.auth.User
import dev.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import java.time.LocalDate

data class LogWorkoutState(
    val date: LocalDate = LocalDate.now(),
    val log: WorkoutLog = WorkoutLog(),
    val user: User? = null,
    val editMode: Boolean = false,
)