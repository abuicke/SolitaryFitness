package com.gravitycode.solitaryfitness.log_workout.data

import dagger.Lazy

class LazyWorkoutLogsRepositoryFactory(
    private val preferencesRepository: Lazy<PreferencesWorkoutLogsRepository>,
    private val firestoreRepository: Lazy<FirestoreWorkoutLogsRepository>
) : WorkoutLogsRepositoryFactory {

    override fun create(isUserSignedIn: Boolean): WorkoutLogsRepository {
        return if (isUserSignedIn) {
            firestoreRepository.get()
        } else {
            preferencesRepository.get()
        }
    }
}