package com.gravitycode.simpletracker.workout_list.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.simpletracker.util.intPreferencesKey
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class WorkoutHistoryRepoImpl(
    private val preferencesStore: DataStore<Preferences>
) : WorkoutHistoryRepo {

    override suspend fun readWorkoutHistory(): Flow<WorkoutHistory> {
        return preferencesStore.data.take(1).map { preferences ->
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
        preferencesStore.edit { preference ->
            for (workout in workouts) {
                val reps = history[workout]
                preference[intPreferencesKey(workout)] = reps
                Log.i("workout_history", "set $workout to $reps")
            }
        }
    }
}