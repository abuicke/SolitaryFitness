package com.gravitycode.solitaryfitness.log_workout.data

interface WorkoutLogsRepositoryFactory {

    data class Configuration(val isUserSignedIn: Boolean)

    fun create(configuration: Configuration): WorkoutLogsRepository
}