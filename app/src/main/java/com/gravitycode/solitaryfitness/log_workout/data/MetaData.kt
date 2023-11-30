package com.gravitycode.solitaryfitness.log_workout.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MetaData<K> {

    fun getRecords(): Flow<K>

    fun containsRecord(key: K): Boolean
}