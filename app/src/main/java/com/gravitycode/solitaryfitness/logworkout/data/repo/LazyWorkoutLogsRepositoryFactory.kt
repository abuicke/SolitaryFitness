package com.gravitycode.solitaryfitness.logworkout.data.repo

import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepositoryFactory
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