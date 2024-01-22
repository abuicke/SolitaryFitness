package com.gravitycode.solitaryfitness.logworkout.data

interface WorkoutLogsRepositoryManager {

    fun getOfflineRepository(): WorkoutLogsRepository

    fun getOnlineRepository(): WorkoutLogsRepository
}