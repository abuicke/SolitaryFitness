package com.gravitycode.solitaryfitnessapp.logworkout.data.repo

interface WorkoutLogsRepositoryFactory {

    fun getOfflineRepository(): WorkoutLogsRepository

    fun getOnlineRepository(): WorkoutLogsRepository
}