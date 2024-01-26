package com.gravitycode.solitaryfitness.logworkout.data.sync

import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.util.ResultOf
import com.gravitycode.solitaryfitness.util.android.Log
import com.gravitycode.solitaryfitness.util.data.DataCorruptionError
import com.gravitycode.solitaryfitness.util.error.IllegalStateError
import com.gravitycode.solitaryfitness.util.error.debugError
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class LazySyncDataService(
    private val sourceRepository: Lazy<WorkoutLogsRepository>,
    private val destinationRepository: Lazy<WorkoutLogsRepository>
) : SyncDataService {

    companion object {

        const val TAG = "LazySyncDataService"
    }

    override suspend fun sync(mode: SyncMode, retryAttempts: Int): Flow<ResultOf<LocalDate>> {
        val sourceRepository = sourceRepository.get()
        val destinationRepository = destinationRepository.get()

        return sourceRepository.metaData.getRecords().map { date ->
            val shouldCopy = when (mode) {
                SyncMode.PRESERVE -> !destinationRepository.metaData.containsRecord(date)
                SyncMode.OVERWRITE -> true
            }

            var result: ResultOf<LocalDate>? = null

            if (shouldCopy) {
                var i = 0
                do {
                    val readResult = sourceRepository.readWorkoutLog(date)
                    val writeResult = if (readResult.isSuccess) {
                        val log = readResult.getOrNull()
                        Log.v(TAG, "syncing record $date...")
                        destinationRepository.writeWorkoutLog(date, log!!)
                    } else {
                        /*
                        * TODO: Could readWorkoutLog fail for other reasons? Would need to check for an
                        *   IOException first? `Result.getOrThrow` may be useful here, combined with a multi-
                        *   level try-catch such as:
                        *
                        *       try {
                        *           result.getOrThrow()
                        *       }catch(ioe: IOException) {
                        *           Result.failure(ioe)
                        *       }catch() {
                        *           throw DataCorruptionError("metadata is out of sync with repository")
                        *       }
                        * */
                        throw DataCorruptionError("metadata is out of sync with repository")
                    }

                    if (writeResult.isSuccess) {
                        Log.v(TAG, "successfully synced record: $date")
                        result = ResultOf.success(date)
                    } else {
                        if (i < retryAttempts) {
                            Log.w(TAG, "Sync failed for $date, retrying...")
                        } else {
                            debugError("Failed to sync record for $date", writeResult)
                            result = ResultOf.failure(date, writeResult.exceptionOrNull()!!)
                        }
                    }
                } while (writeResult.isFailure && i++ < retryAttempts)
            } else {
                Log.d(TAG, "skipped $date")
                result = ResultOf.success(date)
            }

            if (result != null) {
                return@map result
            } else {
                throw IllegalStateError("no result generated for $date")
            }
        }
    }
}