package com.gravitycode.solitaryfitness.log_workout.data

import com.gravitycode.solitaryfitness.util.ResultOf
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

enum class SyncMode {

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

interface SyncDataService {

    /**
     * @param[mode] The sync mode as defined in [SyncMode]
     * @param[retryAttempts] The number of times to re-attempt syncing a record if it fails. Default is 3.
     * */
    suspend fun sync(mode: SyncMode, retryAttempts: Int = 3): Flow<ResultOf<LocalDate>>
}