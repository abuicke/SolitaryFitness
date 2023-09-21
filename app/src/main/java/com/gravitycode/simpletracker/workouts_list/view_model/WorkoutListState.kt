package com.gravitycode.simpletracker.workouts_list.view_model

import com.gravitycode.simpletracker.workouts_list.util.Workout
import java.util.EnumMap

class WorkoutListState {

    /*
    * TODO: Does this need to be synchronized?
    * See https://developer.android.com/reference/kotlin/java/util/EnumMap
    * */
    val workoutReps = EnumMap<Workout, Int>(Workout::class.java)
}