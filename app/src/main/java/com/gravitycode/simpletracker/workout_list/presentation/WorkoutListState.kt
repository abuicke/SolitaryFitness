package com.gravitycode.simpletracker.workout_list.presentation

import com.gravitycode.simpletracker.workout_list.util.Workout

data class WorkoutListState(
    var handstandPressUps: Int = 0,
    var pressUps: Int = 0,
    var sitUps: Int = 0,
    var squats: Int = 0,
    var squatThrusts: Int = 0,
    var burpees: Int = 0,
    var starJumps: Int = 0,
    var stepUps: Int = 0
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

    operator fun set(workout: Workout, reps: Int) {
        when (workout) {
            Workout.HANDSTAND_PRESS_UP -> handstandPressUps = reps
            Workout.PRESS_UP -> pressUps = reps
            Workout.SIT_UP -> sitUps = reps
            Workout.SQUAT -> squats = reps
            Workout.SQUAT_THRUST -> squatThrusts = reps
            Workout.BURPEE -> burpees = reps
            Workout.STAR_JUMP -> starJumps = reps
            Workout.STEP_UP -> stepUps = reps
        }
    }
}