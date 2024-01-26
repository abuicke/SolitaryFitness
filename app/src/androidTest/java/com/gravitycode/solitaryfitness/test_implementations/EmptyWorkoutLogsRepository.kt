package com.gravitycode.solitaryfitness.test_implementations

import com.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.domain.Workout
import com.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.util.data.MetaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class EmptyWorkoutLogsRepository : WorkoutLogsRepository {

    private companion object {

        val success = Result.success(Unit)
    }

    override val metaData = object : MetaData<LocalDate> {

        override fun getRecords(): Flow<LocalDate> = flow {}

        override fun containsRecord(key: LocalDate) = false
    }

    override suspend fun readWorkoutLog(date: LocalDate) = Result.success(null)

    override suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog) = success

    override suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int) = success

    override suspend fun deleteWorkoutLog(date: LocalDate) = success
}