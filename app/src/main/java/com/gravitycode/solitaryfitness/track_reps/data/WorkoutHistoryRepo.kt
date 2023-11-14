package com.gravitycode.solitaryfitness.track_reps.data

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.time.LocalDate

interface WorkoutHistoryRepo {

    suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog?>

    suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit>

    suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int): Result<Unit>

    suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit>
}