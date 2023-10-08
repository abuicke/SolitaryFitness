package com.gravitycode.simpletracker.workout_list.presentation

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
        return when (workout) {
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

    fun copy(workout: Workout, reps: Int): WorkoutListState {
        return when (workout) {
            Workout.HANDSTAND_PRESS_UP -> copy(handstandPressUps = reps)
            Workout.PRESS_UP -> copy(pressUps = reps)
            Workout.SIT_UP -> copy(sitUps = reps)
            Workout.SQUAT -> copy(squats = reps)
            Workout.SQUAT_THRUST -> copy(squatThrusts = reps)
            Workout.BURPEE -> copy(burpees = reps)
            Workout.STAR_JUMP -> copy(starJumps = reps)
            Workout.STEP_UP -> copy(stepUps = reps)
        }
    }
}