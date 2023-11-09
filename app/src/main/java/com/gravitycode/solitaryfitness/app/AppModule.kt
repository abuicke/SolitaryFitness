package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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
    @ApplicationScope
    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Provides
    fun providesFirebaseFirestore() = Firebase.firestore

    @Provides
    @ApplicationScope
    fun provideWorkoutHistoryRepo(
        preferencesStore: DataStore<Preferences>
    ): WorkoutHistoryRepo = PreferencesWorkoutHistoryRepo(preferencesStore)
}