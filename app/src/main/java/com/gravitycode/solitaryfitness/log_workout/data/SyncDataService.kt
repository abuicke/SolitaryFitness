package com.gravitycode.solitaryfitness.log_workout.data

import com.gravitycode.solitaryfitness.util.ResultOf
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SyncDataService {

    /**
     * @param[mode] The sync mode as defined in [SyncMode]
     * @param[retryAttempts] The number of times to re-attempt syncing a record if it fails. Default is 3.
     * */
    suspend fun sync(mode: SyncMode, retryAttempts: Int = 3): Flow<ResultOf<LocalDate>>
}