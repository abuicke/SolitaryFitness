package com.gravitycode.simpletracker.workouts_list.util

import uy.klutter.core.collections.asReadOnly

object Workouts {

    val DEFAULT_WORKOUTS = listOf(
        "Press-Up",
        "Sit-Up",
        "Squat",
        "Squat-Thrust",
        "Burpee",
        "Star Jump",
        "Step-Up"
    ).asReadOnly()
}