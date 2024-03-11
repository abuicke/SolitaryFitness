package com.gravitycode.solitaryfitnessapp.logworkout

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitnessapp.app.FlowLauncher
import com.gravitycode.solitaryfitnessapp.auth.AuthenticationObservable
import com.gravitycode.solitaryfitnessapp.auth.Authenticator
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.firestore.DebugFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.firestore.ProductionFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.preferences.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitnessapp.logworkout.data.sync.LazySyncDataService
import com.gravitycode.solitaryfitnessapp.logworkout.data.sync.SyncDataService
import com.gravitycode.solitaryfitnessapp.logworkout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitnessapp.util.AppConfiguration
import com.gravitycode.solitaryfitnessapp.util.android.Messenger
import com.gravitycode.solitaryfitnessapp.util.android.data.DataStoreManager
import com.gravitycode.solitaryfitnessapp.util.firebase.firestoreSettings
import dagger.Lazy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class InternalDependency

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class OfflineRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class OnlineRepository

@Module
object LogWorkoutModule {

    @Provides
    @InternalDependency
    fun providesWorkoutLogsPreferencesStore(dataStoreManager: DataStoreManager): DataStore<Preferences> {
        return dataStoreManager.datastore("workout_logs")
    }

    @Provides
    @LogWorkoutScope
    @OfflineRepository
    fun provideOfflineWorkoutLogsRepository(
        applicationScope: CoroutineScope,
        @InternalDependency preferencesStore: DataStore<Preferences>
    ): WorkoutLogsRepository {
        return PreferencesWorkoutLogsRepository(applicationScope, preferencesStore)
    }

    @Provides
    @InternalDependency
    fun providesFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings(
            persistentCacheSizeMb = 10
        )

        return firestore
    }

    @Provides
    @LogWorkoutScope
    @OnlineRepository
    fun provideOnlineWorkoutLogsRepository(
        applicationScope: CoroutineScope,
        authenticator: Authenticator,
        @InternalDependency firestore: FirebaseFirestore
    ): WorkoutLogsRepository {
        return if (AppConfiguration.isDebug()) {
            DebugFirestoreWorkoutLogsRepository(applicationScope, authenticator, firestore)
        } else {
            ProductionFirestoreWorkoutLogsRepository(applicationScope, authenticator, firestore)
        }
    }

    @Provides
    @LogWorkoutScope
    fun providesWorkoutLogsRepositoryFactory(
        @OfflineRepository offlineRepository: Lazy<WorkoutLogsRepository>,
        @OnlineRepository onlineRepository: Lazy<WorkoutLogsRepository>
    ): WorkoutLogsRepositoryFactory {
        return LazyWorkoutLogsRepositoryFactory(offlineRepository, onlineRepository)
    }

    @Provides
    @LogWorkoutScope
    fun providesSyncDataService(
        @OfflineRepository offlineRepository: Lazy<WorkoutLogsRepository>,
        @OnlineRepository onlineRepository: Lazy<WorkoutLogsRepository>
    ): SyncDataService {
        return LazySyncDataService(offlineRepository, onlineRepository)
    }

    @Provides
    @LogWorkoutScope
    fun providesLogWorkoutViewModel(
        messenger: Messenger,
        authenticationObservable: AuthenticationObservable,
        flowLauncher: FlowLauncher,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(messenger, authenticationObservable, flowLauncher, factory)
}