package com.gravitycode.solitaryfitness

import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
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
            "WorkoutLog constructor should not accept negative values",
            IllegalArgumentException::class.java
        ) {
            WorkoutLog(
                burpees = -50,
                handstandPressUps = 10,
                squatThrusts = 100
            )
        }
    }

//    @Test
//    fun `assigning negative values in builder set operator throws IllegalArgumentException`() {
//        assertThrows(
//            "WorkoutLog.Builder set operator should not accept a negative value",
//            IllegalArgumentException::class.java
//        ) {
//            val bldr = WorkoutLog.Builder()
//            bldr[Workout.SIT_UP] = -50
//            bldr[Workout.STAR_JUMP] = 12
//            bldr[Workout.HANDSTAND_PRESS_UP] = -15
//        }
//    }
//
//    @Test
//    fun `assigning negative values in builder functions throws IllegalArgumentException`() {
//        assertThrows(
//            "WorkoutLog.Builder functions should not accept negative values",
//            IllegalArgumentException::class.java
//        ) {
//            WorkoutLog.Builder()
//                .handstandPressUps(50)
//                .pressUps(100)
//                .sitUps(30)
//                .squats(50)
//                .squatThrusts(-12)
//                .burpees(-6)
//                .starJumps(500)
//                .stepUps(-100)
//        }
//    }

    @Test
    fun `attempt to use copy(Workout, Int) with a negative value throws IllegalArgumentException`() {
        assertThrows(
            "WorkoutLog.copy(Workout, Int) should not accept a negative value",
            IllegalArgumentException::class.java
        ) {
            val log = WorkoutLog()
            val logCopy = log.copy(squatThrusts = -10)
        }
    }

//    @Test
//    fun `does get() retrieve what is set in the builder`() {
//        val log_1 = WorkoutLog.Builder()
//            .starJumps(15)
//            .burpees(5)
//            .squatThrusts(100)
//            .build()
//
//        assertEquals(15, log_1[Workout.STAR_JUMP])
//        assertEquals(5, log_1[Workout.BURPEE])
//        assertEquals(100, log_1[Workout.SQUAT_THRUST])
//
//        val bldr = WorkoutLog.Builder()
//        bldr[Workout.HANDSTAND_PRESS_UP] = 10
//        bldr[Workout.PRESS_UP] = 50
//        bldr[Workout.STEP_UP] = 1000
//
//        val log_2 = bldr.build()
//
//        assertEquals(10, log_2[Workout.HANDSTAND_PRESS_UP])
//        assertEquals(50, log_2[Workout.PRESS_UP])
//        assertEquals(1000, log_2[Workout.STEP_UP])
//    }

    @Test
    fun `does empty WorkoutLog equal empty WorkoutLog`() {
        assertEquals(
            "empty WorkoutLog objects should be equal",
            WorkoutLog(),
            WorkoutLog()
        )
    }

//    @Test
//    fun `does WorkoutLog from constructor equal WorkoutLog from Builder with the same values`() {
//        val workoutLog_1 = WorkoutLog(
//            handstandPressUps = 15,
//            pressUps = 50,
//            squatThrusts = 30,
//            stepUps = 100
//        )
//
//        val workoutLog_2 = WorkoutLog.Builder()
//            .handstandPressUps(15)
//            .pressUps(50)
//            .squatThrusts(30)
//            .stepUps(100)
//            .build()
//
//        assertEquals(
//            "WorkoutLog objects with the same values should be equal",
//            workoutLog_1,
//            workoutLog_2
//        )
//    }

    @Test
    fun `does hashCode() return the same value for equal WorkoutLog objects`() {
        val workoutLog_1 = WorkoutLog(
            starJumps = 15,
            burpees = 50,
            handstandPressUps = 30
        )

        val workoutLog_2 = WorkoutLog(
            starJumps = 15,
            burpees = 50,
            handstandPressUps = 30
        )

        assertEquals(workoutLog_1, workoutLog_2)
        assertEquals(workoutLog_1.hashCode(), workoutLog_2.hashCode())
    }
}