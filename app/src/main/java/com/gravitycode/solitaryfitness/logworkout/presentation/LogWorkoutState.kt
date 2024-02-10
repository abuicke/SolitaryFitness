package com.gravitycode.solitaryfitness.logworkout.presentation

import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.util.android.Snackbar
import java.time.LocalDate

data class LogWorkoutState(
    val date: LocalDate = LocalDate.now(),
    val log: WorkoutLog = WorkoutLog(),
    val user: User? = null,
    val editMode: Boolean = false,
)