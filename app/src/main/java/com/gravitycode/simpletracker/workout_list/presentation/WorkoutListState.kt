package com.gravitycode.simpletracker.workout_list.presentation

import com.gravitycode.simpletracker.workout_list.util.Workout

/**
 * TODO: This class seems like a mess, duplicating a lot of functionality from [WorkoutHistory]. It
 * could just contain a [WorkoutHistory]?
 *
 * TODO: Is there any purpose in making this a data class?
 * */
class WorkoutListState(
    val handstandPressUps: Int = 0,
    val pressUps: Int = 0,
    val sitUps: Int = 0,
    val squats: Int = 0,
    val squatThrusts: Int = 0,
    val burpees: Int = 0,
    val starJumps: Int = 0,
    val stepUps: Int = 0
) {

    /**
     * TODO: Implement non-null enum map? Does Guava provide non-null maps?
     * */
    constructor(map: Map<Workout, Int>) : this(
        handstandPressUps = map[Workout.HANDSTAND_PRESS_UP]!!,
        pressUps = map[Workout.PRESS_UP]!!,
        sitUps = map[Workout.SIT_UP]!!,
        squats = map[Workout.SQUAT]!!,
        squatThrusts = map[Workout.SQUAT_THRUST]!!,
        burpees = map[Workout.BURPEE]!!,
        starJumps = map[Workout.STAR_JUMP]!!,
        stepUps = map[Workout.STEP_UP]!!
    )

    operator fun get(workout: Workout): Int {
        return when(workout) {
            Workout.HANDSTAND_PRESS_UP -> handstandPressUps
            Workout.PRESS_UP -> pressUps
            Workout.SIT_UP -> sitUps
            Workout.SQUAT -> squats
            Workout.SQUAT_THRUST -> squatThrusts
            Workout.BURPEE -> burpees
            Workout.STAR_JUMP -> starJumps
            Workout.STEP_UP -> stepUps
        }
    }

    fun copy() {
        
    }
}