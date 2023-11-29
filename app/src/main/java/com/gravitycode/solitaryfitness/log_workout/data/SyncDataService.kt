package com.gravitycode.solitaryfitness.log_workout.data

import android.util.Log
import com.gravitycode.solitaryfitness.log_workout.data.SyncDataService.Mode
import com.gravitycode.solitaryfitness.util.data.DataCorruptionError
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Messenger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

interface SyncDataService {

    companion object {

        fun create(
            messenger: Messenger,
            sourceRepository: WorkoutLogsRepository,
            destinationRepository: WorkoutLogsRepository
        ): SyncDataService {
            return SyncDataServiceImpl(messenger, sourceRepository, destinationRepository)
        }
    }

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
    suspend fun sync(mode: Mode, retryAttempts: Int = 3)

}

private class SyncDataServiceImpl(
    private val messenger: Messenger,
    private val sourceRepository: WorkoutLogsRepository,
    private val destinationRepository: WorkoutLogsRepository
) : SyncDataService {

    companion object {

        const val TAG = "SyncDataServiceImpl"
    }

    override suspend fun sync(mode: Mode, retryAttempts: Int) {
        sourceRepository.metaData.getRecords().collect { date ->
            val shouldCopy = when (mode) {
                Mode.PRESERVE -> !destinationRepository.metaData.containsRecord(date)
                Mode.OVERWRITE -> true
            }

            if (shouldCopy) {
                var i = 0
                do {
                    val readResult = sourceRepository.readWorkoutLog(date)
                    val writeResult = if (readResult.isSuccess) {
                        val log = readResult.getOrNull()
                        Log.v(TAG, "syncing record $date...")
                        destinationRepository.writeWorkoutLog(date, log!!)
                    } else {
                        throw DataCorruptionError("metadata is out of sync with repository")
                    }

                    if (writeResult.isSuccess) {
                        Log.v(TAG, "successfully synced record: $date")
                    } else {
                        withContext(Dispatchers.Main) {
                            if (i < retryAttempts) {
                                messenger.toast("Sync failed for $date, retrying...")
                            } else {
                                debugError("Failed to sync record for $date", writeResult)
                                messenger.toast("Sync failed for $date")
                            }
                        }
                    }
                } while (writeResult.isFailure && i++ < retryAttempts)
            } else {
                Log.d(TAG, "skipped $date")
            }
        }
    }
}