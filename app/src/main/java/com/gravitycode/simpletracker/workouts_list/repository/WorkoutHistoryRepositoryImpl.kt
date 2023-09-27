package com.gravitycode.simpletracker.workouts_list.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.simpletracker.util.intPreferencesKey
import com.gravitycode.simpletracker.workouts_list.util.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onErrorResume
import kotlinx.coroutines.flow.take
import java.io.IOException

/**
 * TODO: Inject DataStore with Dagger
 * */
class WorkoutHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : WorkoutHistoryRepository {

    override suspend fun readWorkoutHistory(): Flow<WorkoutHistory> {
        return dataStore.data.take(1).map { preferences ->
            val workoutHistory = WorkoutHistory()
            val workouts = Workout.values()

            for (workout in workouts) {
                val reps = preferences[intPreferencesKey(workout)]
                if (reps != null) {
                    workoutHistory[workout] = reps
                }
            }

            workoutHistory
        }
    }

    override suspend fun writeWorkoutHistory(history: WorkoutHistory) {
        val workouts = Workout.values()
        dataStore.edit { preference ->
            for (workout in workouts) {
                val reps = history[workout]
                preference[intPreferencesKey(workout)] = reps
                Log.i("workout_history", "set $workout to $reps")
            }
        }
    }
}