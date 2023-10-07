package com.gravitycode.simpletracker.workout_list.data

import androidx.annotation.IntRange
import com.gravitycode.simpletracker.workout_list.util.Workout
import java.util.EnumMap

/**
 * TODO: Should there be an immutable version of this object for passing to classes such as
 * [com.gravitycode.simpletracker.workout_list.domain.WorkoutListState]?
 * */
class WorkoutHistory(
    private val history: EnumMap<Workout, Int> = EnumMap<Workout, Int>(Workout::class.java)
) {

    constructor(map: Map<Workout, Int>) : this(EnumMap<Workout, Int>(map))

    init {
        for (workout in Workout.values()) {
            val reps = history[workout]
            if (reps == null) {
                history[workout] = 0
            } else if (reps < 0) {
                throw IllegalArgumentException(
                    "cannot assign negative reps value: $workout = $reps"
                )
            }
        }
    }

    @IntRange(from = 0) operator fun get(workout: Workout): Int {
        return history[workout]!!
    }

    operator fun set(workout: Workout, @IntRange(from = 0) reps: Int) {
        if (reps < 0) throw IllegalArgumentException(
            "reps cannot be less than zero, reps provided: $reps"
        )
        history[workout] = reps
    }

    operator fun inc(workout: Workout): WorkoutHistory = inc(workout, 1)

    /**
     * TODO: Why is operator allowed on this function? It seems like any number of arguments is okay.
     * Need to create mirror function in `dec()` once I understand what's going on here. Does []
     * accept multiple arguments if they're provided in the operator function?
     * */
    operator fun inc(workout: Workout, quantity: Int): WorkoutHistory {
        history[workout] = history[workout]!! + quantity
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

    override fun equals(other: Any?): Boolean {
        return if (other is WorkoutHistory) {
            history == other.history
        } else {
            false
        }
    }

    override fun hashCode() = history.hashCode()

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