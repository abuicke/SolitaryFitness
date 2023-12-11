package com.gravitycode.solitaryfitness.log_workout.data

import com.gravitycode.solitaryfitness.util.ResultOf
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SyncDataService {

    enum class Mode {

        /**
         * If a record already exists in the destination repository,
         * don't replace it with the record from the source directory.
         * */
        PRESERVE,

        /**
         * If a record already exists in the destination repository,
         * replace it with the record from the source repository.
         * */
        OVERWRITE
    }

    /**
     * @param[mode] The sync mode as defined in [Mode]
     * @param[retryAttempts] The number of times to re-attempt syncing a record if it fails. Default is 3.
     * */
    suspend fun sync(mode: Mode, retryAttempts: Int = 3): Flow<ResultOf<LocalDate>>
}