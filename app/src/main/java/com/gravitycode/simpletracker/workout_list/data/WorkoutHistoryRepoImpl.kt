package com.gravitycode.simpletracker.workout_list.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.simpletracker.util.data.intPreferencesKey
import com.gravitycode.simpletracker.workout_list.domain.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.time.LocalDate

class WorkoutHistoryRepoImpl(
    private val preferencesStore: DataStore<Preferences>
) : WorkoutHistoryRepo {

    override suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutHistory> {
        return preferencesStore.data.take(1).map { preferences ->
            val workoutHistory = WorkoutHistory()
            val workouts = Workout.values()

            for (workout in workouts) {
                val reps = preferences[intPreferencesKey(date, workout)]
                if (reps != null) {
                    workoutHistory[workout] = reps
                }
            }

            workoutHistory
        }
    }

    override suspend fun writeWorkoutHistory(
        date: LocalDate,
        history: WorkoutHistory
    ): Result<Unit> {
        val workouts = Workout.values()
        return try {
            preferencesStore.edit { preference ->
                for (workout in workouts) {
                    val reps = history[workout]
                    preference[intPreferencesKey(date, workout)] = reps
                }
            }

            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}