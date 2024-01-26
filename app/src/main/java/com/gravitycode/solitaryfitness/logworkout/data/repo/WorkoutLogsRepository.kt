package com.gravitycode.solitaryfitness.logworkout.data.repo

import com.gravitycode.solitaryfitness.logworkout.domain.Workout
import com.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.util.data.MetaData
import java.time.LocalDate

interface WorkoutLogsRepository {

    val metaData: MetaData<LocalDate>

    suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog?>

    suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit>

    suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int): Result<Unit>

    suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit>
}