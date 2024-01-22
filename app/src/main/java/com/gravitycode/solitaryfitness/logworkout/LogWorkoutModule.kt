package com.gravitycode.solitaryfitness.logworkout

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.BuildConfig
import com.gravitycode.solitaryfitness.app.AppState
import com.gravitycode.solitaryfitness.app.FlowLauncher
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.logworkout.data.LazySyncDataService
import com.gravitycode.solitaryfitness.logworkout.data.LazyWorkoutLogsRepositoryManager
import com.gravitycode.solitaryfitness.logworkout.data.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.SyncDataService
import com.gravitycode.solitaryfitness.logworkout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.WorkoutLogsRepositoryManager
import com.gravitycode.solitaryfitness.logworkout.data.firestore.DebugFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.firestore.ProductionFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.data.firestoreSettings
import com.gravitycode.solitaryfitness.util.ui.Messenger
import dagger.Lazy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
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
        return if (BuildConfig.DEBUG) {
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
    ): WorkoutLogsRepositoryManager {
        return LazyWorkoutLogsRepositoryManager(offlineRepository, onlineRepository)
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
        appStateFlow: Flow<AppState>,
        flowLauncher: FlowLauncher,
        messenger: Messenger,
        factory: WorkoutLogsRepositoryManager
    ) = LogWorkoutViewModel(appStateFlow, flowLauncher, messenger, factory)
}