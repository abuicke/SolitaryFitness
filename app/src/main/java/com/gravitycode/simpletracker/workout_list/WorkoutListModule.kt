package com.gravitycode.simpletracker.workout_list

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class WorkoutListModule {

    @Provides @ActivityScope fun providesWorkoutHistoryDataStore(
        context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Provides @ActivityScope fun provideWorkoutHistoryRepository(
        dataStore: DataStore<Preferences>
    ): WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)
}