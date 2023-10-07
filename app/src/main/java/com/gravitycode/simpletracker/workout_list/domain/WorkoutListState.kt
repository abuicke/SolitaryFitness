package com.gravitycode.simpletracker.workout_list.domain

import androidx.annotation.IntRange
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.util.Workout

/**
 * TODO: This class seems like a mess, duplicating a lot of functionality from [WorkoutHistory]. It
 * could just contain a [WorkoutHistory]?
 * */
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

    /**
     * TODO: This seems like a real disaster and I should just use the map, or something more sensible.
     * */
    operator fun get(@IntRange(from = 0, to = 7) index: Int): Int {
        return when (index) {
            0 -> handstandPressUps
            1 -> pressUps
            2 -> sitUps
            3 -> squats
            4 -> squatThrusts
            5 -> burpees
            6 -> starJumps
            7 -> stepUps
            else -> throw IllegalStateException(
                "index must be between 0 and 7 inclusive"
            )
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

    operator fun set(workout: Workout, @IntRange(from = 0) reps: Int) {
        if (reps < 0) throw IllegalArgumentException(
            "reps cannot be less than zero, reps provided: $reps"
        )

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

    /**
     * TODO: Should this have `operator` keyword? See TODO in [WorkoutHistory.inc]
     * */
    fun inc(workout: Workout, @IntRange(from = 1) quantity: Int) {
        if (quantity < 1) throw IllegalArgumentException(
            "increment can't be less than 1"
        )

        set(workout, get(workout) + quantity)
    }
}