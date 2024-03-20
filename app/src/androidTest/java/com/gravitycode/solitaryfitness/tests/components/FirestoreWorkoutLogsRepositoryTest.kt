package dev.gravitycode.solitaryfitness.tests.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dev.gravitycode.solitaryfitness.logworkout.LogWorkoutModule
import dev.gravitycode.solitaryfitness.logworkout.data.repo.firestore.TestingFirestoreWorkoutLogsRepository
import dev.gravitycode.solitaryfitness.logworkout.domain.Workout
import dev.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import dev.gravitycode.solitaryfitness.test_implementations.TestUsers
import dev.gravitycode.solitaryfitness.test_utils.assertSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
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

        private val firestore = LogWorkoutModule.providesFirebaseFirestore()
        private val repository = TestingFirestoreWorkoutLogsRepository(
            TestScope(),
            TestUsers.ADAM_BUICKE,
            firestore,
        )

        @AfterClass
        @JvmStatic
        @JvmName("staticClearTestRecords")
        fun clearTestRecords() {
            runBlocking {
                repository.deleteAll()
            }
        }
    }

    @Before
    fun clearTestRecords() {
        Companion.clearTestRecords()
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