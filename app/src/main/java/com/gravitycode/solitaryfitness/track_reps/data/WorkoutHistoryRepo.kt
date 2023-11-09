package com.gravitycode.solitaryfitness.track_reps.data

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutHistoryRepo {

    suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutLog>

    suspend fun writeWorkoutHistory(date: LocalDate, history: WorkoutLog): Result<Unit>
}