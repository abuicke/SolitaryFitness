package com.gravitycode.solitaryfitness.log_workout.data

import dagger.Lazy
import javax.annotation.concurrent.ThreadSafe

@ThreadSafe
class LazyWorkoutLogsRepositoryFactory(
    private val offlineRepository: Lazy<WorkoutLogsRepository>,
    private val onlineRepository: Lazy<WorkoutLogsRepository>
) : WorkoutLogsRepositoryFactory {

    override fun getOfflineRepository(): WorkoutLogsRepository = offlineRepository.get()

    override fun getOnlineRepository(): WorkoutLogsRepository = onlineRepository.get()
}