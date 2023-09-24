package com.gravitycode.simpletracker.workouts_list.repository

import android.content.Context
import io.reactivex.rxjava3.core.Single

interface WorkoutHistoryRepository {

    /**
     * TODO: Remove [Context] parameter
     * */
    suspend fun getWorkoutHistory(context: Context): Single<WorkoutHistory>
}