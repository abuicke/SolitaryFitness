package com.gravitycode.solitaryfitness.di

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator
import com.gravitycode.solitaryfitness.log_workout.data.FirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
private annotation class Private

@Module
object ActivityModule {

    @Provides
    @ActivityScope
    fun providesAuthenticator(activity: ComponentActivity, auth: FirebaseAuth, ui: AuthUI): Authenticator {
        return FirebaseAuthenticator(activity, auth, ui)
    }

    @Private
    @Provides
    @ActivityScope
    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Private
    @Provides
    @ActivityScope
    fun providePreferencesWorkoutHistoryRepo(
        @Private preferencesStore: DataStore<Preferences>
    ) = PreferencesWorkoutLogsRepository(preferencesStore)

    @Private
    @Provides
    @ActivityScope
    fun provideFirestoreWorkoutLogsRepository(
        firestore: FirebaseFirestore,
        authenticator: Authenticator
    ) = FirestoreWorkoutLogsRepository(firestore, authenticator)

    @Provides
    @ActivityScope
    fun providesWorkoutLogsRepositoryFactory(
        @Private preferencesRepository: Lazy<PreferencesWorkoutLogsRepository>,
        @Private firestoreRepository: Lazy<FirestoreWorkoutLogsRepository>
    ): WorkoutLogsRepositoryFactory {
        return LazyWorkoutLogsRepositoryFactory(
            preferencesRepository,
            firestoreRepository
        )
    }
}