package com.gravitycode.solitaryfitness.logworkout.data

interface WorkoutLogsRepositoryFactory {

    fun getOfflineRepository(): WorkoutLogsRepository

    fun getOnlineRepository(): WorkoutLogsRepository
}