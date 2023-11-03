package com.gravitycode.solitaryfitness.track_reps.data

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutHistory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutHistoryRepo {

    suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutHistory>

    suspend fun writeWorkoutHistory(date: LocalDate, history: WorkoutHistory): Result<Unit>
}