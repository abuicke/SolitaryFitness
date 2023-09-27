package com.gravitycode.simpletracker.workout_list.repository

import kotlinx.coroutines.flow.Flow

interface WorkoutHistoryRepository {

    suspend fun readWorkoutHistory(): Flow<WorkoutHistory>

    suspend fun writeWorkoutHistory(history: WorkoutHistory)
}