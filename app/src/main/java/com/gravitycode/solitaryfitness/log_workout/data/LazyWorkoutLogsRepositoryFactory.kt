package com.gravitycode.solitaryfitness.log_workout.data

import dagger.Lazy
import javax.annotation.concurrent.ThreadSafe

/**
 * TODO: From what I understand, this will reconstruct the repositories and their dependencies each time
 *  (because I haven't tied the repos or their parent dependencies to any lifecycle). Need to verify this
 *  behaviour.
 * */
@ThreadSafe
class LazyWorkoutLogsRepositoryFactory(
    private val offlineRepository: Lazy<WorkoutLogsRepository>,
    private val onlineRepository: Lazy<WorkoutLogsRepository>
) : WorkoutLogsRepositoryFactory {

    private companion object {

        const val OFFLINE = 0
        const val ONLINE = 1
    }

    private val LOCK = Any()

    private var currentRepoKey: Int? = null
    private var currentRepo: WorkoutLogsRepository? = null

    override fun getOfflineRepository(): WorkoutLogsRepository {
        synchronized(LOCK) {
            if (currentRepoKey == ONLINE || currentRepoKey == null) {
                currentRepoKey = OFFLINE
                currentRepo = offlineRepository.get()
            }

            return currentRepo!!
        }
    }

    override fun getOnlineRepository(): WorkoutLogsRepository {
        synchronized(LOCK) {
            if (currentRepoKey == OFFLINE || currentRepoKey == null) {
                currentRepoKey = ONLINE
                currentRepo = onlineRepository.get()
            }

            return currentRepo!!
        }
    }
}