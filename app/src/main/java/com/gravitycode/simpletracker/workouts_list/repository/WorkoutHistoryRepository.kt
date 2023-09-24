package com.gravitycode.simpletracker.workouts_list.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface WorkoutHistoryRepository {

    /**
     * TODO: Remove [Context] parameter
     * */
    suspend fun readWorkoutHistory(context: Context): WorkoutHistory //Single<WorkoutHistory>

    suspend fun writeWorkoutHistory(context: Context, workoutHistory: WorkoutHistory)
}