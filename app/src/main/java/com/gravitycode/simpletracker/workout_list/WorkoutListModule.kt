package com.gravitycode.simpletracker.workout_list

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepoImpl
import dagger.Module
import dagger.Provides

@Module
class WorkoutListModule {

    @Provides
    @ActivityScope
    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Provides
    @ActivityScope
    fun provideWorkoutHistoryRepo(
        preferencesStore: DataStore<Preferences>
    ): WorkoutHistoryRepo = WorkoutHistoryRepoImpl(preferencesStore)
}