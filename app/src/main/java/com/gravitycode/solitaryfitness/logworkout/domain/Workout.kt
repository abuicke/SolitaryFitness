package com.gravitycode.solitaryfitness.logworkout.domain

enum class Workout(val string: String) {

    HANDSTAND_PRESS_UP("Handstand Press-Up"),
    PRESS_UP("Press-Up"),
    SIT_UP("Sit-Up"),
    SQUAT("Squat"),
    SQUAT_THRUST("Squat-Thrust"),
    BURPEE("Burpee"),
    STAR_JUMP("Star Jump"),
    STEP_UP("Step-Up");

    companion object {

        fun fromString(str: String): Workout? {
            for (item in values()) {
                if (item.string == str) {
                    return item
                }
            }

            return null
        }
    }
}