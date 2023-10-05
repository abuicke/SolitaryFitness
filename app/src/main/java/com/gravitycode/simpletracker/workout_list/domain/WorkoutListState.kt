package com.gravitycode.simpletracker.workout_list.domain

import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.util.Workout

data class WorkoutListState(
    val handstandPressUps: Int = 0,
    val pressUps: Int = 0,
    val sitUps: Int = 0,
    val squats: Int = 0,
    val squatThrusts: Int = 0,
    val burpees: Int = 0,
    val starJumps: Int = 0,
    val stepUps: Int = 0
) {
    // TODO: Test
    companion object {
        fun from(history: WorkoutHistory) = WorkoutListState(
            handstandPressUps = history[Workout.HANDSTAND_PRESS_UP],
            pressUps = history[Workout.PRESS_UP],
            sitUps = history[Workout.SIT_UP],
            squats = history[Workout.SQUAT],
            squatThrusts = history[Workout.SQUAT_THRUST],
            burpees = history[Workout.BURPEE],
            starJumps = history[Workout.STAR_JUMP],
            stepUps = history[Workout.STEP_UP]
        )
    }
}