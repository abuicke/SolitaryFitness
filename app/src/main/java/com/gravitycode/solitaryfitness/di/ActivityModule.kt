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
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.ui.Toaster
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

    @Provides
    @ActivityScope
    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("workout_history")
        }
    }

    @Private
    @Provides
    fun providePreferencesWorkoutHistoryRepo(
        preferencesStore: DataStore<Preferences>
    ) = PreferencesWorkoutLogsRepository(preferencesStore)

    @Private
    @Provides
    fun provideFirestoreWorkoutLogsRepository(
        firestore: FirebaseFirestore,
        authenticator: Authenticator
    ) = FirestoreWorkoutLogsRepository(firestore, authenticator)

    @Provides
    @ActivityScope
    fun provideLogWorkoutViewModel(
        toaster: Toaster,
        repo: WorkoutLogsRepository
    ) = LogWorkoutViewModel(toaster, repo)

    @Provides
    @ActivityScope
    fun providesWorkoutLogsRepositoryFactory(
        preferencesRepository: Lazy<PreferencesWorkoutLogsRepository>,
        firestoreRepository: Lazy<FirestoreWorkoutLogsRepository>
    ): WorkoutLogsRepositoryFactory {
        return LazyWorkoutLogsRepositoryFactory(
            preferencesRepository,
            firestoreRepository
        )
    }
}