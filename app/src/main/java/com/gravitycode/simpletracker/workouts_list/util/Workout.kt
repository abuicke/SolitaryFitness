package com.gravitycode.simpletracker.workouts_list.util

enum class Workout(private val prettyString: String) {

    PRESS_UP("Press-Up"),
    SIT_UP("Sit-Up"),
    SQUAT("Squat"),
    SQUAT_THRUST("Squat Thrust"),
    BURPEE("Burpee"),
    STAR_JUMP("Star Jump"),
    STEP_UP("Step-up");

    /**
     * For rendering on UI
     * */
    fun toPrettyString() = prettyString
}