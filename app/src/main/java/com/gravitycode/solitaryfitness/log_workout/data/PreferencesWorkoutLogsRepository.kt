package com.gravitycode.solitaryfitness.log_workout.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.data.MetaData
import com.gravitycode.solitaryfitness.util.data.intPreferencesKey
import com.gravitycode.solitaryfitness.util.data.stringSetPreferencesKey
import com.gravitycode.solitaryfitness.util.error.debugError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class PreferencesWorkoutLogsRepository(
    appController: AppController,
    private val preferencesStore: DataStore<Preferences>
) : WorkoutLogsRepository {

    private val _metaData = PreferencesMetaData(appController.applicationScope, preferencesStore)
    override val metaData: MetaData<LocalDate> = _metaData

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

            _metaData.add(date)
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

            _metaData.remove(date)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}

private class PreferencesMetaData(
    scope: CoroutineScope,
    private val preferencesStore: DataStore<Preferences>
) : MetaData<LocalDate> {

    private companion object {

        const val TAG = "PreferencesMetaData"

        val DATES_KEY = stringSetPreferencesKey("dates")
    }

    /*
    * Using HashSet as long as the order of keys does not need to be maintained.
    * Switch implementation to LinkedHashSet if the contract with MetaData changes.
    * */
    private val records = HashSet<String>()

    init {
        /**
         * TODO: Should there also be error handling here as in FirestoreMetaData?
         * */
        scope.launch(Dispatchers.IO) {
            val preferences = preferencesStore.data.first()
            val dates: Set<String>? = preferences[DATES_KEY]

            if (dates != null) {
                this@PreferencesMetaData.records.addAll(dates)
            }
        }
    }

    override fun getRecords(): Flow<LocalDate> {
        return records.asFlow().map { dateStr ->
            LocalDate.parse(dateStr)
        }
    }

    override fun containsRecord(key: LocalDate) = records.contains(key.toString())

    suspend fun add(date: LocalDate) {
        preferencesStore.edit { preferences ->
            val success = records.add(date.toString())
            if (success) {
                preferences[DATES_KEY] = records
                Log.v(TAG, "successfully added record $date to meta data")
            } else {
                debugError("failed to add record $date to meta data")
            }
        }
    }

    suspend fun remove(date: LocalDate) {
        preferencesStore.edit { preferences ->
            val success = records.remove(date.toString())
            if (success) {
                preferences[DATES_KEY] = records
                Log.v(TAG, "successfully removed record $date from meta data")
            } else {
                debugError("failed to remove record $date from meta data")
            }
        }
    }
}