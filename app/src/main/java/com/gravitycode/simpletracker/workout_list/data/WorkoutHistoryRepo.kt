package com.gravitycode.simpletracker.workout_list.data

import com.gravitycode.simpletracker.workout_list.domain.WorkoutHistory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutHistoryRepo {

    suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutHistory>

    suspend fun writeWorkoutHistory(history: WorkoutHistory): Result<Unit>
}