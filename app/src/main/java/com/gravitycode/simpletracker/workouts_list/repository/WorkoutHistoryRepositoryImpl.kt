package com.gravitycode.simpletracker.workouts_list.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gravitycode.simpletracker.workouts_list.util.Workout
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take

// TODO: Move to Dagger
// TODO: Switch to [RxDataStore] - https://developer.android.com/reference/kotlin/androidx/datastore/preferences/rxjava3/package-summary
// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_history")

/**
 * TODO: Should cache retrieved value
 * TODO: Need to inject DataStore to prevent duplication and easy mocking
 * */
class WorkoutHistoryRepositoryImpl : WorkoutHistoryRepository {

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

    /**
     * TODO: Use [reduce] to get the effect I want?
     * */
    override suspend fun readWorkoutHistory(context: Context): WorkoutHistory {

        val workoutHistory = WorkoutHistory()
        val workouts = Workout.values()

        context.dataStore.data.collect { preferences ->
            for (workout in workouts) {
                val workoutName = workout.toPrettyString()
                val count = preferences[intPreferencesKey(workout.toString())]
                workoutHistory[workout] = count!!
                Log.i("workout_history", "read: $workoutName: $count")
            }
        }

        return workoutHistory

//        return Single.just(WorkoutHistory())
    }

    override suspend fun writeWorkoutHistory(context: Context, workoutHistory: WorkoutHistory) {
        val workouts = Workout.values()
        var i = 0
        context.dataStore.edit { preference ->
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