package com.gravitycode.solitaryfitness.log_workout.data

import dagger.Lazy

data class Configuration(val isUserSignedIn: Boolean)

interface WorkoutLogsRepositoryFactory {

    fun create(configuration: Configuration): WorkoutLogsRepository
}

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