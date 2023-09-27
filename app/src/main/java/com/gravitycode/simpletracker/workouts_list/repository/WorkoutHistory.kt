package com.gravitycode.simpletracker.workouts_list.repository

import androidx.annotation.IntRange
import com.gravitycode.simpletracker.workouts_list.util.Workout
import java.util.Collections
import java.util.EnumMap

/**
 * TODO: Map is synchronized. Does it need to be?
 * See [https://developer.android.com/reference/java/util/EnumMap]
 * */
class WorkoutHistory(
    private val history: EnumMap<Workout, Int> = Collections.synchronizedMap(
        EnumMap<Workout, Int>(Workout::class.java)
    ) as EnumMap<Workout, Int>
) {

    init {
        // Add each key from com.gravitycode.simpletracker.workouts_list.util.Workout
        // into the map ahead of time and provide each entry with a default value.
        for (workout in Workout.values()) {
            history[workout] = 0
        }
    }

    operator fun get(workout: Workout): Int {
        return history[workout]!!
    }

    operator fun set(workout: Workout, @IntRange(from = 0) count: Int) {
        if (count < 0) throw IllegalArgumentException(
            "count cannot be less than zero, count provided is $count"
        )
        history[workout] = count
    }

    operator fun inc(workout: Workout): WorkoutHistory {
        history[workout] = history[workout]!! + 1
        return WorkoutHistory(history)
    }

    /**
     * Dec operator doesn't really make sense for this
     * use-case, but implementing it for parity's sake.
     * */
    operator fun dec(workout: Workout): WorkoutHistory {
        history[workout] = history[workout]!! - 1
        return WorkoutHistory(history)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('{')
        builder.append('\n')
        for (workout in Workout.values()) {
            builder.append(
                "\t${workout.toPrettyString()}: ${history[workout]}\n"
            )
        }
        builder.append('}')

        return builder.toString()
    }
}