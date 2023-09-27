package com.gravitycode.simpletracker.workouts_list.util

/**
 * @param[prettyString] a string which is returned by
 * [toPrettyString] for rendering on-screen to the user
 * */
enum class Workout(private val prettyString: String) {

    HANDSTAND_PRESS_UP("Handstand Press-Up"),
    PRESS_UP("Press-Up"),
    SIT_UP("Sit-Up"),
    SQUAT("Squat"),
    SQUAT_THRUST("Squat-Thrust"),
    BURPEE("Burpee"),
    STAR_JUMP("Star Jump"),
    STEP_UP("Step-Up");

    /**
     * For rendering on UI
     *
     * @return UI presentable string
     * */
    fun toPrettyString() = prettyString
}