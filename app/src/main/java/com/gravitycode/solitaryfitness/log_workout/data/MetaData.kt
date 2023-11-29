package com.gravitycode.solitaryfitness.log_workout.data

import kotlinx.coroutines.flow.Flow

interface MetaData<RecordKey> {

    fun getRecords(): Flow<RecordKey>

    fun containsRecord(key: RecordKey): Boolean
}