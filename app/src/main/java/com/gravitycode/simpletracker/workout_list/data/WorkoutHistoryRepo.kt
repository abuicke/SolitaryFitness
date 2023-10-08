package com.gravitycode.simpletracker.workout_list.data

import com.gravitycode.simpletracker.workout_list.domain.WorkoutHistory
import kotlinx.coroutines.flow.Flow

interface WorkoutHistoryRepo {

    suspend fun readWorkoutHistory(): Flow<WorkoutHistory>

    suspend fun writeWorkoutHistory(history: WorkoutHistory): Result<Unit>
}