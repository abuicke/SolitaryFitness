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
import dagger.Binds
import javax.inject.Singleton

@Module
class AppModule {

    @Provides fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides fun providesWorkoutHistoryDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    /**
     * TODO: Replace with binding like this [https://stackoverflow.com/a/53635909/4596649]
     * */
    @Provides fun provideWorkoutHistoryRepository(
        dataStore: DataStore<Preferences>
    ): WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)
}