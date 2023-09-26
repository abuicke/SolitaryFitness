package com.gravitycode.simpletracker.workouts_list.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.simpletracker.util.intPreferencesKey
import com.gravitycode.simpletracker.workouts_list.util.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * TODO: Inject DataStore with Dagger
 * TODO: Go back to generating all keys at the beginning if it seems not to have been the problem.
 * */
class WorkoutHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : WorkoutHistoryRepository {

    override suspend fun readWorkoutHistory(): Flow<WorkoutHistory> {
        return dataStore.data.map { preferences ->
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

    override suspend fun writeWorkoutHistory() {
        val workouts = Workout.values()
        dataStore.edit { preference ->
            for (workout in workouts) {
                val reps = (0..10).random()
                preference[intPreferencesKey(workout)] = reps
                Log.i(
                    "workout_history",
                    "updated: $workout: $reps"
                )
            }
        }
    }
}