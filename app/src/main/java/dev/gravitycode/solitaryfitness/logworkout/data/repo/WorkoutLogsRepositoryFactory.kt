package dev.gravitycode.solitaryfitness.logworkout.data.repo

interface WorkoutLogsRepositoryFactory {

    fun getOfflineRepository(): WorkoutLogsRepository

    fun getOnlineRepository(): WorkoutLogsRepository
}