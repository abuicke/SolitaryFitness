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
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
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

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class Offline

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class Online

@Module
object LogWorkoutModule {

    @Private
    @Provides
    fun providesWorkoutLogsPreferencesStore(
        context: Context
    ) = createPreferencesStoreFromFile(context, "workout_logs")

    @Offline
    @Provides
    fun provideOfflineWorkoutLogsRepository(
        appController: AppController,
        @Private preferencesStore: DataStore<Preferences>
    ): WorkoutLogsRepository {
        return PreferencesWorkoutLogsRepository(appController, preferencesStore)
    }

    @Private
    @Provides
    fun providesFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings(
            persistentCacheSizeMb = 10
        )

        return firestore
    }

    @Online
    @Provides
    fun provideOnlineWorkoutLogsRepository(
        @Private firestore: FirebaseFirestore,
        authenticator: Authenticator
    ): WorkoutLogsRepository {
        return FirestoreWorkoutLogsRepository(firestore, authenticator)
    }

    @Provides
    @ActivityScope
    fun providesWorkoutLogsRepositoryFactory(
        @Offline offlineRepository: Lazy<WorkoutLogsRepository>,
        @Online onlineRepository: Lazy<WorkoutLogsRepository>
    ): WorkoutLogsRepositoryFactory {
        return LazyWorkoutLogsRepositoryFactory(
            offlineRepository,
            onlineRepository
        )
    }

    @Provides
    @ActivityScope
    fun providesSyncDataService(
        messenger: Messenger,
        @Offline offlineRepository: WorkoutLogsRepository,
        @Online onlineRepository: WorkoutLogsRepository
    ) = SyncDataService.create(messenger, offlineRepository, onlineRepository)

    @Provides
    @ActivityScope
    fun providesLogWorkoutViewModel(
        appController: AppController,
        messenger: Messenger,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(appController, messenger, factory)
}