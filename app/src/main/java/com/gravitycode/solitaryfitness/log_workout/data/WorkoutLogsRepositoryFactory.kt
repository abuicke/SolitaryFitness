package com.gravitycode.solitaryfitness.log_workout.data

interface WorkoutLogsRepositoryFactory {

    fun getInstance(isUserSignedIn: Boolean): WorkoutLogsRepository
}