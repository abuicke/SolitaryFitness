package com.gravitycode.solitaryfitness

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.gravitycode.solitaryfitness.app.AppModule
import com.gravitycode.solitaryfitness.test_implementations.TestFirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.assertSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FirestoreWorkoutLogsRepositoryTest {

    private companion object {

        private val firestore = AppModule.providesFirebaseFirestore()
        private val repository = TestFirestoreWorkoutLogsRepository(
            firestore,
            "Adam Buicke",
            "buickea@gmail.com",
            "https://lh3.googleusercontent.com/a/ACg8ocL8p9etpQsdZNTxhdbz_lVknHmZY44d2QE6landGS3Lbws=s96-c"
        )

        @AfterClass
        @JvmStatic
        @JvmName("staticClearTestRecords")
        fun clearTestRecords() {
            runBlocking {
                repository.clearTestRecords()
            }
        }
    }

    @Before
    fun clearTestRecords() {
        FirestoreWorkoutLogsRepositoryTest.clearTestRecords()
    }

    @Test
    fun readEmptyRecord() {
        runTest {
            val date = LocalDate.now()
            val result = repository.readWorkoutLog(date)
            assertSuccess(result)
            assertEquals(null, result.getOrNull())
        }
    }

    @Test
    fun writeRecord() {
        runTest {
            val date = LocalDate.now()
            val workoutLog = WorkoutLog(
                pressUps = 15,
                handstandPressUps = 5,
                starJumps = 25
            )

            repository.writeWorkoutLog(date, workoutLog)
            val result = repository.readWorkoutLog(date)

            assertSuccess(result)
            assertEquals(workoutLog, result.getOrNull())
        }
    }

    @Test
    fun updateRecord() {
        runTest {
            val date = LocalDate.now()
            val workoutLog = WorkoutLog(
                squats = 50,
                squatThrusts = 25,
                sitUps = 100
            )

            repository.writeWorkoutLog(date, workoutLog)
            repository.updateWorkoutLog(date, Workout.SQUAT_THRUST, 50)

            val updatedLog = workoutLog.copy(squatThrusts = 50)

            val result = repository.readWorkoutLog(date)

            assertSuccess(result)
            assertEquals(updatedLog, result.getOrNull())
        }
    }

    @Test
    fun deleteRecord() {
        runTest {
            val date = LocalDate.now()
            val workoutLog = WorkoutLog(
                squats = 50,
                squatThrusts = 25,
                sitUps = 100
            )

            repository.writeWorkoutLog(date, workoutLog)
            repository.deleteWorkoutLog(date)

            val result = repository.readWorkoutLog(date)

            assertSuccess(result)
            assertEquals(null, result.getOrNull())
        }
    }
}