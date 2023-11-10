package com.gravitycode.solitaryfitness.track_reps.data

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import java.time.LocalDate

interface WorkoutHistoryRepo {

    suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog>

    suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit>

//    suspend fun readWorkoutHistory(): Result<WorkoutHistory>
//
//    suspend fun writeWorkoutHistory(history: WorkoutHistory): Result<Unit>
}