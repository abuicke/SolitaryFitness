package com.gravitycode.simpletracker.track_reps

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gravitycode.simpletracker.app.ActivityScope
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.track_reps.data.WorkoutHistoryRepoImpl
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsViewModelImpl
import dagger.Module
import dagger.Provides

@Module
object TrackRepsModule {

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

    @Provides
    @ActivityScope
    fun provideTrackRepsViewModel(repo: WorkoutHistoryRepo): TrackRepsViewModel =
        TrackRepsViewModelImpl(repo)
}