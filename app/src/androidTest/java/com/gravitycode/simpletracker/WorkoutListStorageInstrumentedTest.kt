package com.gravitycode.simpletracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * TODO: `@Before` functions should be replaced with `@BeforeClass`, but instance
 * variables will then have to be initialized statically, so in the class companion object?
 * */
@MediumTest
@RunWith(AndroidJUnit4::class)
class WorkoutListStorageInstrumentedTest {

    private lateinit var applicationContext: Context
    private lateinit var workoutHistoryDataStore: DataStore<Preferences>
    private lateinit var workoutHistoryRepository: WorkoutHistoryRepository

    @Before
    fun loadApplicationContext() {
        applicationContext = ApplicationProvider.getApplicationContext()
    }

    @Before
    fun loadWorkoutHistoryTestDataStore() {
        workoutHistoryDataStore = PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile("test_workout_history")
        }
    }

    @Before
    fun loadWorkoutHistoryRepository() {
        workoutHistoryRepository = WorkoutHistoryRepositoryImpl(workoutHistoryDataStore)
    }

    @Test
    fun testReadAndWriteWorkoutHistoryRepository() {
        runTest {

        }
    }

    @After
    fun clearTestDataStore() {

    }
}