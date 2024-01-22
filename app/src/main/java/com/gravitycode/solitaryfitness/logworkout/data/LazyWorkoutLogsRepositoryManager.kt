package com.gravitycode.solitaryfitness.logworkout.data

import dagger.Lazy
import javax.annotation.concurrent.ThreadSafe

@ThreadSafe
class LazyWorkoutLogsRepositoryManager(
    private val offlineRepository: Lazy<WorkoutLogsRepository>,
    private val onlineRepository: Lazy<WorkoutLogsRepository>
) : WorkoutLogsRepositoryManager {

    override fun getOfflineRepository(): WorkoutLogsRepository = offlineRepository.get()

    override fun getOnlineRepository(): WorkoutLogsRepository = onlineRepository.get()
}