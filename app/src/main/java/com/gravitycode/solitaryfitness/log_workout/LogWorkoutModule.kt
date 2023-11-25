package com.gravitycode.solitaryfitness.log_workout

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.di.ActivityScope
import com.gravitycode.solitaryfitness.log_workout.data.FirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.data.createPreferencesStoreFromFile
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class Private

@Module
object LogWorkoutModule {

    @Private
    @Provides
    @ActivityScope
    fun providesWorkoutLogsPreferencesStore(
        context: Context
    ) = createPreferencesStoreFromFile(context, "workout_logs")

    @Private
    @Provides
    @ActivityScope
    fun providePreferencesWorkoutLogsRepo(
        appController: AppController,
        @Private preferencesStore: DataStore<Preferences>
    ) = PreferencesWorkoutLogsRepository(appController, preferencesStore)

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

    @Provides
    @ActivityScope
    fun providesLogWorkoutViewModel(
        appController: AppController,
        toaster: Toaster,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(appController, toaster, factory)
}