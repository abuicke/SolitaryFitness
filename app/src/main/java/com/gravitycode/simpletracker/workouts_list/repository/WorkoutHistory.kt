package com.gravitycode.simpletracker.workouts_list.repository

import com.gravitycode.simpletracker.workouts_list.util.Workout
import java.util.EnumMap
import androidx.annotation.IntRange
import kotlin.jvm.Throws

/**
 * TODO: Does this need to be thread-safe? If so see how to synchronize EnumMap in [EnumMap]
 * */
class WorkoutHistory(
    private val history: EnumMap<Workout, Int> = EnumMap<Workout, Int>(Workout::class.java)
) {

    init {
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
     * Dec operator doesn't really make sense for this use-case,
     * but implementing it for parity's sake
     * */
    operator fun dec(workout: Workout): WorkoutHistory {
        history[workout] = history[workout]!! - 1
        return WorkoutHistory(history)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('{')
        builder.append('\n')
        for(workout in Workout.values()) {
            builder.append(
                "\t${workout.toPrettyString()}: ${history[workout]}\n"
            )
        }
        builder.append('}')

        return builder.toString()
    }
}