package com.gravitycode.solitaryfitness.track_reps.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import com.gravitycode.solitaryfitness.util.data.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.time.LocalDate

class PreferencesWorkoutHistoryRepo(
    private val preferencesStore: DataStore<Preferences>
) : WorkoutHistoryRepo {

    override suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutLog> {
        return preferencesStore.data.take(1).map { preferences ->
            val workoutLog = WorkoutLog()
            val workouts = Workout.values()

            for (workout in workouts) {
                val reps = preferences[intPreferencesKey(date, workout)]
                if (reps != null) {
                    workoutLog[workout] = reps
                }
            }

            workoutLog
        }
    }

    override suspend fun writeWorkoutHistory(
        date: LocalDate,
        history: WorkoutLog
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