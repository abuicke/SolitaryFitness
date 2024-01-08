package com.gravitycode.solitaryfitness.log_workout.domain

import com.gravitycode.solitaryfitness.log_workout.util.Workout

data class WorkoutLog(
    val handstandPressUps: Int = 0,
    val pressUps: Int = 0,
    val sitUps: Int = 0,
    val squats: Int = 0,
    val squatThrusts: Int = 0,
    val burpees: Int = 0,
    val starJumps: Int = 0,
    val stepUps: Int = 0
) {

    companion object {

        fun from(map: Map<Workout, Int>) = WorkoutLog(
            handstandPressUps = if (map.containsKey(Workout.HANDSTAND_PRESS_UP)) map[Workout.HANDSTAND_PRESS_UP]!! else 0,
            pressUps = if (map.containsKey(Workout.PRESS_UP)) map[Workout.PRESS_UP]!! else 0,
            sitUps = if (map.containsKey(Workout.SIT_UP)) map[Workout.SIT_UP]!! else 0,
            squats = if (map.containsKey(Workout.SQUAT)) map[Workout.SQUAT]!! else 0,
            squatThrusts = if (map.containsKey(Workout.SQUAT_THRUST)) map[Workout.SQUAT_THRUST]!! else 0,
            burpees = if (map.containsKey(Workout.BURPEE)) map[Workout.BURPEE]!! else 0,
            starJumps = if (map.containsKey(Workout.STAR_JUMP)) map[Workout.STAR_JUMP]!! else 0,
            stepUps = if (map.containsKey(Workout.STEP_UP)) map[Workout.STEP_UP]!! else 0
        )
    }

    init {
        require(handstandPressUps >= 0) {
            "reps cannot be less than zero, reps specified for handstand press-ups = $handstandPressUps"
        }
        require(pressUps >= 0) {
            "reps cannot be less than zero, reps specified for press-ups = $pressUps"
        }
        require(sitUps >= 0) {
            "reps cannot be less than zero, reps specified for handstand sit-ups = $sitUps"
        }
        require(squats >= 0) {
            "reps cannot be less than zero, reps specified for handstand squats = $squats"
        }
        require(squatThrusts >= 0) {
            "reps cannot be less than zero, reps specified for handstand squat-thrusts = $squatThrusts"
        }
        require(burpees >= 0) {
            "reps cannot be less than zero, reps specified for handstand burpees = $burpees"
        }
        require(starJumps >= 0) {
            "reps cannot be less than zero, reps specified for handstand star jumps = $starJumps"
        }
        require(stepUps >= 0) {
            "reps cannot be less than zero, reps specified for handstand step-ups = $stepUps"
        }
    }

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

    fun toMap() = hashMapOf(
        Workout.HANDSTAND_PRESS_UP to handstandPressUps,
        Workout.PRESS_UP to pressUps,
        Workout.SIT_UP to sitUps,
        Workout.SQUAT to squats,
        Workout.SQUAT_THRUST to squatThrusts,
        Workout.BURPEE to burpees,
        Workout.STAR_JUMP to starJumps,
        Workout.STEP_UP to stepUps
    )

    fun copy(workout: Workout, reps: Int): WorkoutLog {
        require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
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