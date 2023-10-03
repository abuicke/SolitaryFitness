package com.gravitycode.simpletracker

import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.util.Workout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class WorkoutHistoryTest {

    @Test
    fun `does empty WorkoutHistory equal empty WorkoutHistory`() {
        assertEquals(
            "empty WorkoutHistory objects should be equal",
            WorkoutHistory(),
            WorkoutHistory()
        )
    }

    @Test
    fun `does partial WorkoutHistory equal partial WorkoutHistory with the same values`() {
        val workoutHistory_1 = WorkoutHistory(
            mapOf(
                Workout.HANDSTAND_PRESS_UP to 15,
                Workout.PRESS_UP to 50,
                Workout.SQUAT_THRUST to 30,
                Workout.STEP_UP to 100
            )
        )

        val workoutHistory_2 = WorkoutHistory()
        workoutHistory_2[Workout.HANDSTAND_PRESS_UP] = 15
        workoutHistory_2[Workout.PRESS_UP] = 50
        workoutHistory_2[Workout.SQUAT_THRUST] = 30
        workoutHistory_2[Workout.STEP_UP] = 100

        assertEquals(
            "WorkoutHistory objects with the same values should be equal",
            workoutHistory_1,
            workoutHistory_2
        )
    }

    @Test
    fun `does hashCode() return the same value for equal WorkoutHistory objects`() {
        val workoutHistory_1 = WorkoutHistory(
            mapOf(
                Workout.STAR_JUMP to 15,
                Workout.BURPEE to 50,
                Workout.HANDSTAND_PRESS_UP to 30,
            )
        )

        val workoutHistory_2 = WorkoutHistory(
            mapOf(
                Workout.STAR_JUMP to 15,
                Workout.BURPEE to 50,
                Workout.HANDSTAND_PRESS_UP to 30,
            )
        )

        assertEquals(workoutHistory_1, workoutHistory_2)
        assertEquals(workoutHistory_1.hashCode(), workoutHistory_2.hashCode())
    }

    @Test
    fun `assign negative values to WorkoutHistory - in constructor`() {
        assertThrows(
            "WorkoutHistory should not accept negative values",
            IllegalArgumentException::class.java
        ) {
            WorkoutHistory(
                mapOf(
                    Workout.BURPEE to -50,
                    Workout.HANDSTAND_PRESS_UP to 10,
                    Workout.SQUAT_THRUST to 100
                )
            )
        }
    }

    @Test
    fun `assign negative values to WorkoutHistory - in setter`() {
        assertThrows(
            "WorkoutHistory should not accept negative values",
            IllegalArgumentException::class.java
        ) {
            val history = WorkoutHistory()
            history[Workout.SIT_UP] = -50
            history[Workout.STAR_JUMP] = 12
            history[Workout.HANDSTAND_PRESS_UP] = -15
        }
    }
}