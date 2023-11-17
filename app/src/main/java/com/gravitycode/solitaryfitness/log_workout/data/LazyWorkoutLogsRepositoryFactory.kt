package com.gravitycode.solitaryfitness.log_workout.data

import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory.Configuration
import dagger.Lazy

class LazyWorkoutLogsRepositoryFactory(
    private val preferencesRepository: Lazy<PreferencesWorkoutLogsRepository>,
    private val firestoreRepository: Lazy<FirestoreWorkoutLogsRepository>
) : WorkoutLogsRepositoryFactory {

    override fun create(configuration: Configuration): WorkoutLogsRepository {
        return if (configuration.isUserSignedIn) {
            firestoreRepository.get()
        } else {
            preferencesRepository.get()
        }
    }
}