package com.gravitycode.simpletracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepoImpl
import com.gravitycode.simpletracker.track_reps.domain.WorkoutHistory
import com.gravitycode.simpletracker.track_reps.util.Workout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * Essential unit tests
 *
 * When following best practice, you should ensure you use unit tests in the following cases:
 *      1)  Unit tests for ViewModels, or presenters.
 *      2)  Unit tests for the data layer, especially repositories. Most of the data layer should
 *          be platform-independent. Doing so enables test doubles to replace database modules and
 *          remote data sources in tests. See the guide on using test doubles in Android
 *      3)  Unit tests for other platform-independent layers such as the Domain layer, as with
 *          use cases and interactors.
 *      4)  Unit tests for utility classes such as string manipulation and math.
 *
 * [https://developer.android.com/training/testing/fundamentals/what-to-test]
 *
 * */
@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TrackRepsStorageInstrumentedTest {

    companion object {

        private const val TEST_DATA_STORE = "test_workout_history"
        private val testDate = LocalDate.of(2000, 1, 1)

        private val applicationContext: Context = ApplicationProvider.getApplicationContext()
        private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile(TEST_DATA_STORE)
        }
        private val repository: WorkoutHistoryRepo = WorkoutHistoryRepoImpl(dataStore)

        @AfterClass
        @JvmStatic
        @JvmName("staticClearTestDataStore")
        fun clearTestDataStore() {
            runTest {
                dataStore.edit { preferences ->
                    preferences.clear()
                }
            }
        }
    }

    @Before
    fun clearTestDataStore() {
        TrackRepsStorageInstrumentedTest.clearTestDataStore()
    }

    @Test
    fun readEmptyRepository() {
        runTest {
            repository.readWorkoutHistory(testDate).collect { workoutHistory ->
                assertEquals(WorkoutHistory(), workoutHistory)
            }
        }
    }

    @Test
    fun writeAndReadRepository() {
        runTest {

            val workoutHistory = WorkoutHistory(
                mapOf(
                    Workout.PRESS_UP to 15,
                    Workout.HANDSTAND_PRESS_UP to 5,
                    Workout.STAR_JUMP to 25
                )
            )

            repository.writeWorkoutHistory(testDate, workoutHistory)

            repository.readWorkoutHistory(testDate).collect { wH ->
                assertEquals(workoutHistory, wH)
            }
        }
    }
}