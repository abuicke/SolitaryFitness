package com.gravitycode.simpletracker.workouts_list.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface WorkoutHistoryRepository {

    suspend fun readWorkoutHistory(): Flow<WorkoutHistory>

    suspend fun writeWorkoutHistory(history: WorkoutHistory)
}