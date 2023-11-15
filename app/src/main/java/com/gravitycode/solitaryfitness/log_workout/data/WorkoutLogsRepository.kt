package com.gravitycode.solitaryfitness.log_workout.data

import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import java.time.LocalDate

interface WorkoutLogsRepository {

    suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog?>

    suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit>

    suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int): Result<Unit>

    suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit>
}