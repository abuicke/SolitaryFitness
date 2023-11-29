package com.gravitycode.solitaryfitness.log_workout

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.log_workout.data.SyncDataService
import com.gravitycode.solitaryfitness.util.data.firestoreSettings
import com.gravitycode.solitaryfitness.di.ActivityScope
import com.gravitycode.solitaryfitness.log_workout.data.FirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.data.createPreferencesStoreFromFile
import com.gravitycode.solitaryfitness.util.ui.Messenger
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
    fun providesFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings(
            persistentCacheSizeMb = 10
        )

        return firestore
    }

    @Private
    @Provides
    @ActivityScope
    fun provideFirestoreWorkoutLogsRepository(
        @Private firestore: FirebaseFirestore,
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
    fun providesSyncDataService(
        @Private preferencesRepository: PreferencesWorkoutLogsRepository,
        @Private firestoreRepository: FirestoreWorkoutLogsRepository
    ) = SyncDataService.create(preferencesRepository, firestoreRepository)

    @Provides
    @ActivityScope
    fun providesLogWorkoutViewModel(
        appController: AppController,
        messenger: Messenger,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(appController, messenger, factory)
}