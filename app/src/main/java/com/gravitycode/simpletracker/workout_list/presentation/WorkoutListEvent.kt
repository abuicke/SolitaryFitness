package com.gravitycode.simpletracker.workout_list.presentation

import com.gravitycode.simpletracker.workout_list.util.Workout

sealed class WorkoutListEvent(val workout: Workout) {

    class Increment(wO: Workout, val quantity: Int) : WorkoutListEvent(wO)
}