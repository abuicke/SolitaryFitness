package com.gravitycode.solitaryfitness.log_workout.data

interface WorkoutLogsRepositoryFactory {

    fun getOfflineRepository(): WorkoutLogsRepository

    fun getOnlineRepository(): WorkoutLogsRepository
}