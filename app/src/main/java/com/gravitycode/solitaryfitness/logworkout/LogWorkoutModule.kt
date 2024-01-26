package com.gravitycode.solitaryfitness.logworkout

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.BuildConfig
import com.gravitycode.solitaryfitness.app.AppState
import com.gravitycode.solitaryfitness.app.FlowLauncher
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.logworkout.data.repo.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.logworkout.data.repo.firestore.DebugFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.repo.firestore.ProductionFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.repo.preferences.PreferencesWorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.sync.LazySyncDataService
import com.gravitycode.solitaryfitness.logworkout.data.sync.SyncDataService
import com.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.android.Messenger
import com.gravitycode.solitaryfitness.util.android.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.firebase.firestoreSettings
import dagger.Lazy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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
        appStateFlow: Flow<AppState>,
        flowLauncher: FlowLauncher,
        messenger: Messenger,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(appStateFlow, flowLauncher, messenger, factory)
}