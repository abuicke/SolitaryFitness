package com.gravitycode.solitaryfitness.log_workout.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.data.intPreferencesKey
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class PreferencesWorkoutLogsRepository(
    private val preferencesStore: DataStore<Preferences>
) : WorkoutLogsRepository {

    override suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog?> {
        val preferences = preferencesStore.data.first()

        val workouts = Workout.values()
        val firstKey = intPreferencesKey(date, workouts[0])
        val recordExists = preferences.contains(firstKey)

        return if (recordExists) {
            val workoutLog = WorkoutLog(
                handstandPressUps = preferences[intPreferencesKey(date, Workout.HANDSTAND_PRESS_UP)]!!,
                pressUps = preferences[intPreferencesKey(date, Workout.PRESS_UP)]!!,
                sitUps = preferences[intPreferencesKey(date, Workout.SIT_UP)]!!,
                squats = preferences[intPreferencesKey(date, Workout.SQUAT)]!!,
                squatThrusts = preferences[intPreferencesKey(date, Workout.SQUAT_THRUST)]!!,
                burpees = preferences[intPreferencesKey(date, Workout.BURPEE)]!!,
                starJumps = preferences[intPreferencesKey(date, Workout.STAR_JUMP)]!!,
                stepUps = preferences[intPreferencesKey(date, Workout.STEP_UP)]!!
            )
            Result.success(workoutLog)
        } else {
            Result.success(null)
        }
    }

    override suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit> {
        return try {
            val workouts = Workout.values()
            preferencesStore.edit { preferences ->
                for (workout in workouts) {
                    val reps = log[workout]
                    preferences[intPreferencesKey(date, workout)] = reps
                }
            }

            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int): Result<Unit> {
        require(reps >= 0) { "reps cannot be less than zero, reps provided: $reps" }
        return try {
            preferencesStore.edit { preferences ->
                preferences[intPreferencesKey(date, workout)] = reps
            }

            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit> {
        return try {
            val workouts = Workout.values()
            preferencesStore.edit { preferences ->
                for (workout in workouts) {
                    val key = intPreferencesKey(date, workout)
                    preferences.remove(key)
                }
            }

            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}