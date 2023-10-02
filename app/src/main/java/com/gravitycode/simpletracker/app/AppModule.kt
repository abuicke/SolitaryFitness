package com.gravitycode.simpletracker.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workout_list.domain.WorkoutListViewModel
import dagger.Module
import dagger.Provides

/**
 * TODO: Can this module be turned into an object? Will [Application] still be injected?
 * */
@Module
class AppModule {

    @Provides fun providesApplicationContext(app: Application): Context = app

    @Provides fun providesWorkoutHistoryDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Provides fun provideWorkoutHistoryRepository(
        dataStore: DataStore<Preferences>
    ): WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)

    @Provides fun providesWorkoutListViewModel(
        workoutHistoryRepository: WorkoutHistoryRepository
    ): WorkoutListViewModel = WorkoutListViewModel(workoutHistoryRepository)
}