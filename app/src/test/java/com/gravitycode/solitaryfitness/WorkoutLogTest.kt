package com.gravitycode.solitaryfitness

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Essential unit tests
 *
 * When following best practice, you should ensure you use unit tests in the following cases:
 *      1)  Unit tests for ViewModels, or presenters.
 *      2)  Unit tests for the data layer, especially repositories. Most of the data layer should
 *          be platform-independent. Doing so enables test doubles to replace database modules and
 *          remote data sources in tests. See the guide on using test doubles in Android
 *      3)  Unit tests for other platform-independent layers such as the Domain layer, as with
 *          use cases and interactors.
 *      4)  Unit tests for utility classes such as string manipulation and math.
 *
 * [https://developer.android.com/training/testing/fundamentals/what-to-test]
 *
 * */
class WorkoutLogTest {

    @Test
    fun `empty WorkoutLog returns zero for all values`() {
        val workoutLog = WorkoutLog()
        for (workout in Workout.values()) {
            assertEquals(0, workoutLog[workout])
        }
    }

    @Test
    fun `assigning negative values in constructor throws IllegalArgumentException`() {
        assertThrows(
            "WorkoutLog should not accept negative values",
            IllegalArgumentException::class.java
        ) {
            WorkoutLog(
                mapOf(
                    Workout.BURPEE to -50,
                    Workout.HANDSTAND_PRESS_UP to 10,
                    Workout.SQUAT_THRUST to 100
                )
            )
        }
    }

    @Test
    fun `assigning negative values in setter throws IllegalArgumentException`() {
        assertThrows(
            "WorkoutLog should not accept negative values",
            IllegalArgumentException::class.java
        ) {
            val log = WorkoutLog()
            log[Workout.SIT_UP] = -50
            log[Workout.STAR_JUMP] = 12
            log[Workout.HANDSTAND_PRESS_UP] = -15
        }
    }

    @Test
    fun `does get() retrieve what set() sets`() {
        val workoutLog = WorkoutLog()

        workoutLog[Workout.STAR_JUMP] = 15
        workoutLog[Workout.BURPEE] = 5
        workoutLog[Workout.SQUAT_THRUST] = 100

        assertEquals(15, workoutLog[Workout.STAR_JUMP])
        assertEquals(5, workoutLog[Workout.BURPEE])
        assertEquals(100, workoutLog[Workout.SQUAT_THRUST])
    }

    @Test
    fun `does empty WorkoutLog equal empty WorkoutLog`() {
        assertEquals(
            "empty WorkoutLog objects should be equal",
            WorkoutLog(),
            WorkoutLog()
        )
    }

    @Test
    fun `does partial WorkoutLog equal partial WorkoutLog with the same values`() {
        val workoutLog_1 = WorkoutLog(
            mapOf(
                Workout.HANDSTAND_PRESS_UP to 15,
                Workout.PRESS_UP to 50,
                Workout.SQUAT_THRUST to 30,
                Workout.STEP_UP to 100
            )
        )

        val workoutLog_2 = WorkoutLog()
        workoutLog_2[Workout.HANDSTAND_PRESS_UP] = 15
        workoutLog_2[Workout.PRESS_UP] = 50
        workoutLog_2[Workout.SQUAT_THRUST] = 30
        workoutLog_2[Workout.STEP_UP] = 100

        assertEquals(
            "WorkoutLog objects with the same values should be equal",
            workoutLog_1,
            workoutLog_2
        )
    }

    @Test
    fun `does hashCode() return the same value for equal WorkoutLog objects`() {
        val workoutLog_1 = WorkoutLog(
            mapOf(
                Workout.STAR_JUMP to 15,
                Workout.BURPEE to 50,
                Workout.HANDSTAND_PRESS_UP to 30,
            )
        )

        val workoutLog_2 = WorkoutLog(
            mapOf(
                Workout.STAR_JUMP to 15,
                Workout.BURPEE to 50,
                Workout.HANDSTAND_PRESS_UP to 30,
            )
        )

        assertEquals(workoutLog_1, workoutLog_2)
        assertEquals(workoutLog_1.hashCode(), workoutLog_2.hashCode())
    }
}