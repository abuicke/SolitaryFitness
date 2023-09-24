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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

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

    override suspend fun getWorkoutHistory(context: Context): Single<WorkoutHistory> {

        val workouts = Workout.values()
        var i = 0
        context.dataStore.data.map { preferences ->
            val workoutName = workouts[i].toPrettyString()
            val count = preferences[intPreferencesKey(workouts[i++].toString())]
            /**
             * I think I'm supposed to return something here? I have no idea what this is even doing
             * */
            Log.i("read_work_history", "$workoutName: $count")
        }


//        for (key in preferenceKeys) {
//            context.dataStore.edit { workout_history ->
//                workout_history[key] = (0..10).random()
//                Log.i("workout_history", workout_history[key].toString())
//            }
//        }

        return Single.just(WorkoutHistory())
    }
}