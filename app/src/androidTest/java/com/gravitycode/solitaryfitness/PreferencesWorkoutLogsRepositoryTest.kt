package com.gravitycode.solitaryfitness

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.gravitycode.solitaryfitness.log_workout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.assertSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * @SmallTest: <200ms, no external dependencies or resource access such as file system, network, or
 * databases. Tests that interact with hardware, make binder calls, or facilitate android instrumentation
 * should not use this annotation.
 *
 * @MediumTest: <1000ms, focused on a very limited subset of components or a single component. Resource
 * access to the file system through well-defined interfaces like databases, or [android.content.Context].
 * Network access should be restricted. Long-running or blocking operations should be avoided. Use fake
 * objects instead.
 *
 * @LargeTest: >1000ms, tests the app as a whole, e.g. UI tests. These tests fully participate in the
 * system and may make use of all resources such as databases, file systems, and networks.
 * */
//@MediumTest
//@RunWith(AndroidJUnit4::class)
//@ExperimentalCoroutinesApi
class PreferencesWorkoutLogsRepositoryTest {

//    companion object {
//
//        private const val TEST_DATA_STORE = "test_workout_history"
//        private val testDate = LocalDate.of(2000, 1, 1)
//
//        private val applicationContext: Context = ApplicationProvider.getApplicationContext()
//        private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
//            applicationContext.preferencesDataStoreFile(TEST_DATA_STORE)
//        }
//        private val repository: WorkoutLogsRepository = PreferencesWorkoutLogsRepository(dataStore)
//
//        @AfterClass
//        @JvmStatic
//        @JvmName("staticClearTestDataStore")
//        fun clearTestDataStore() {
//            runBlocking {
//                dataStore.edit { preferences ->
//                    preferences.clear()
//                }
//            }
//        }
//    }
//
//    @Before
//    fun clearTestDataStore() {
//        PreferencesWorkoutLogsRepositoryTest.clearTestDataStore()
//    }
//
//    @Test
//    fun readEmptyRecord() {
//        runTest {
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(null, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun writeAndReadRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                pressUps = 15,
//                handstandPressUps = 5,
//                starJumps = 25
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(workoutLog, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun updateRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                squats = 50,
//                squatThrusts = 25,
//                sitUps = 100
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            repository.updateWorkoutLog(testDate, Workout.SQUAT_THRUST, 50)
//
//            val updatedLog = workoutLog.copy(squatThrusts = 50)
//
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(updatedLog, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun deleteRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                squats = 50,
//                squatThrusts = 25,
//                sitUps = 100
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            repository.deleteWorkoutLog(testDate)
//
//            val result = repository.readWorkoutLog(testDate)
//
//            assertSuccess(result)
//            assertEquals(null, result.getOrNull())
//        }
//    }
}