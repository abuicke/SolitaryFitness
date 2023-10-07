package com.gravitycode.simpletracker.workout_list.domain

import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.util.Workout
import java.util.EnumMap

/**
 * TODO: This class seems like a mess, duplicating a lot of functionality from [WorkoutHistory]. It
 * could just contain a [WorkoutHistory]?
 *
 * TODO: Is there any purpose in making this a data class?
 * */
class WorkoutListState(
    val map: EnumMap<Workout, Int> = EnumMap(Workout::class.java)
) {

}