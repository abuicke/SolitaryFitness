package com.gravitycode.solitaryfitness.test_implementations

import com.gravitycode.solitaryfitness.log_workout.data.MetaData
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
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