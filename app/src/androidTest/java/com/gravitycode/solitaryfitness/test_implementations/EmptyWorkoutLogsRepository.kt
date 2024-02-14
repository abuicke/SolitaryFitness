package com.gravitycode.solitaryfitnessapp.test_implementations

import com.gravitycode.solitaryfitnessapp.logworkout.data.repo.WorkoutLogsRepository
import com.gravitycode.solitaryfitnessapp.logworkout.domain.Workout
import com.gravitycode.solitaryfitnessapp.logworkout.domain.WorkoutLog
import com.gravitycode.solitaryfitnessapp.util.data.MetaData
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