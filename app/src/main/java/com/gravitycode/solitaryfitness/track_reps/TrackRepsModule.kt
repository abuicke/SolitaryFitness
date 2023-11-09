package com.gravitycode.solitaryfitness.track_reps

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.gravitycode.solitaryfitness.app.ActivityScope
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator
import com.gravitycode.solitaryfitness.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.data.PreferencesWorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module
object TrackRepsModule {

    @Provides
    @ActivityScope
    fun providesAuthenticator(activity: ComponentActivity, toaster: Toaster): Authenticator =
        FirebaseAuthenticator(activity, toaster)

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
    ): WorkoutHistoryRepo = PreferencesWorkoutHistoryRepo(preferencesStore)

    @Provides
    @ActivityScope
    fun provideTrackRepsViewModel(toaster: Toaster, repo: WorkoutHistoryRepo): TrackRepsViewModel =
        TrackRepsViewModel(toaster, repo)
}