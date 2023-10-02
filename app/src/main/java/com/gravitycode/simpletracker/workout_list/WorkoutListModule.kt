package com.gravitycode.simpletracker.workout_list

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModel
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workout_list.domain.WorkoutListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO: Should apply `@Binds` to [provideWorkoutHistoryRepository]
 * TODO: [WorkoutListViewModel] should use `@Inject` constructor injection
 * */
@Module
class WorkoutListModule {

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