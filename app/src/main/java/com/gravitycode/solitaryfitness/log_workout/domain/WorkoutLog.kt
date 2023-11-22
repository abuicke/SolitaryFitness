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

    class Builder {

        private var handstandPressUps: Int = 0
        private var pressUps: Int = 0
        private var sitUps: Int = 0
        private var squats: Int = 0
        private var squatThrusts: Int = 0
        private var burpees: Int = 0
        private var starJumps: Int = 0
        private var stepUps: Int = 0

        operator fun set(workout: Workout, reps: Int) {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
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

        fun handstandPressUps(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            handstandPressUps = reps
            return this
        }

        fun pressUps(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            pressUps = reps
            return this
        }

        fun sitUps(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            sitUps = reps
            return this
        }

        fun squats(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            squats = reps
            return this
        }

        fun squatThrusts(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            squatThrusts = reps
            return this
        }

        fun burpees(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            burpees = reps
            return this
        }

        fun starJumps(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            starJumps = reps
            return this
        }

        fun stepUps(reps: Int): Builder {
            require(reps >= 0) { "reps cannot be less than zero, reps specified = $reps" }
            stepUps = reps
            return this
        }

        fun build(): WorkoutLog {
            return WorkoutLog(
                handstandPressUps = handstandPressUps,
                pressUps = pressUps,
                sitUps = sitUps,
                squats = squats,
                squatThrusts = squatThrusts,
                burpees = burpees,
                starJumps = starJumps,
                stepUps = stepUps
            )
        }
    }
}