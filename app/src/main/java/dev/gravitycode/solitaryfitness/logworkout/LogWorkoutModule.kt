package dev.gravitycode.solitaryfitness.logworkout

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import dev.gravitycode.caimito.kotlin.core.AppConfiguration
import dev.gravitycode.caimito.kotlin.ui.Messenger
import dev.gravitycode.solitaryfitness.app.FlowLauncher
import dev.gravitycode.solitaryfitness.auth.AuthenticationStatus
import dev.gravitycode.solitaryfitness.auth.Authenticator
import dev.gravitycode.solitaryfitness.logworkout.data.repo.LazyWorkoutLogsRepositoryFactory
import dev.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepositoryFactory
import dev.gravitycode.solitaryfitness.logworkout.data.repo.firestore.DebugFirestoreWorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.data.repo.firestore.ProductionFirestoreWorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.data.repo.preferences.PreferencesWorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.data.sync.LazySyncDataService
import dev.gravitycode.solitaryfitness.logworkout.data.sync.SyncDataService
import dev.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutViewModel
import dev.gravitycode.solitaryfitness.util.data.DataStoreManager
import dev.gravitycode.solitaryfitness.util.firestoreSettings
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
        authenticationStatus: AuthenticationStatus,
        flowLauncher: FlowLauncher,
        factory: WorkoutLogsRepositoryFactory
    ) = LogWorkoutViewModel(messenger, authenticationStatus, flowLauncher, factory)
}