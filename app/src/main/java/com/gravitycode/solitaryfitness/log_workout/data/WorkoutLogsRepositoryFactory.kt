package com.gravitycode.solitaryfitness.log_workout.data

interface WorkoutLogsRepositoryFactory {

    fun create(isUserSignedIn: Boolean): WorkoutLogsRepository
}