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
import javax.inject.Singleton

/**
 * TODO: Can this module be turned into an object? Will [Application] still be injected?
 * TODO: Need to use Android scopes, e.g. @ActivityScope
 * */
@Module
class ApplicationModule(
    private val app: Application
) {

    @Provides fun providesApplicationContext(): Context = app

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