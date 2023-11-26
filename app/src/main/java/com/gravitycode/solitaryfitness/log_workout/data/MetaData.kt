package com.gravitycode.solitaryfitness.log_workout.data

import kotlinx.coroutines.flow.Flow

interface MetaData<RecordsKey> {

    fun getExistingRecords(): Flow<RecordsKey>

    fun containsRecord(key: RecordsKey): Boolean
}