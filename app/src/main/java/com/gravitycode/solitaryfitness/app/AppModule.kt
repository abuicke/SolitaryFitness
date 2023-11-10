package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.track_reps.data.PreferencesWorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ActivityComponent::class])
object AppModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster(context)

    @Provides
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesFirebaseAuthUi() = AuthUI.getInstance()

    @Provides
    @ApplicationScope
    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Provides
    @ApplicationScope
    fun provideWorkoutHistoryRepo(preferencesStore: DataStore<Preferences>): WorkoutHistoryRepo =
        PreferencesWorkoutHistoryRepo(preferencesStore)
}