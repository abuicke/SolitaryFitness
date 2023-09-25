package com.gravitycode.simpletracker.workouts_list.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.gravitycode.simpletracker.workouts_list.util.Workout

/**
 * TODO: Should cache retrieved value (invalidate cache when write function is called)
 * TODO: Inject DataStore with Dagger
 * */
class WorkoutHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : WorkoutHistoryRepository {

    /**
     * TODO: Replace with Map? Should be more explicit
     * */
    private val preferenceKeys: Array<Preferences.Key<Int>>

    init {
        val workouts = Workout.values()
        preferenceKeys = Array(workouts.size) { index ->
            intPreferencesKey(workouts[index].toString())
        }
    }

    override suspend fun readWorkoutHistory(): WorkoutHistory {

        val workoutHistory = WorkoutHistory()
        val workouts = Workout.values()

        dataStore.data.collect { preferences ->
            for (i in workouts.indices) {
                val workout = workouts[i]
                val workoutName = workout.toPrettyString()
                val reps = preferences[preferenceKeys[i]]
                if (reps != null) {
                    workoutHistory[workout] = reps
                }
                Log.i("workout_history", "read: $workoutName: $reps")
            }
        }

        return workoutHistory
    }

    override suspend fun writeWorkoutHistory() {
        val workouts = Workout.values()
        var i = 0
        dataStore.edit { preference ->
            for (key in preferenceKeys) {
                preference[key] = (0..10).random()
                Log.i(
                    "workout_history",
                    "updated: ${workouts[i]} to ${preference[key].toString()}"
                )
                i++
            }
        }
    }
}