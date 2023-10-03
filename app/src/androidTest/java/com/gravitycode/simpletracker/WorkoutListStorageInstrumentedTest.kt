package com.gravitycode.simpletracker

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * TODO: Check to see if retaining the [java.io.File] object from [preferencesDataStoreFile]
 * allows me to delete the preferencesDataStoreFile after it's been cleared. Check that file
 * exists afterwards and that it's returned a valid completion code.
 * */
@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WorkoutListStorageInstrumentedTest {

    companion object {

        private const val TEST_DATA_STORE = "test_workout_history"

        private val applicationContext: Context = ApplicationProvider.getApplicationContext()
        private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile(TEST_DATA_STORE)
        }
        private val repository: WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)

        @JvmStatic
        @AfterClass
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
        WorkoutListStorageInstrumentedTest.clearTestDataStore()
    }

    @Test
    fun readEmptyRepository() {
        runTest {
            repository.readWorkoutHistory().collect { workoutHistory ->
                Log.i("test_workout_history", workoutHistory.toString())
            }
        }
    }
}