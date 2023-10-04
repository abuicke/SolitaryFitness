package com.gravitycode.simpletracker.workout_list.data

import androidx.annotation.IntRange
import com.gravitycode.simpletracker.workout_list.util.Workout
import net.jcip.annotations.ThreadSafe
import java.util.EnumMap

@ThreadSafe
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

//    @Synchronized
    @IntRange(from = 0)
    operator fun get(workout: Workout): Int {
        return history[workout]!!
    }

    operator fun set(workout: Workout, @IntRange(from = 0) reps: Int) {
        if (reps < 0) throw IllegalArgumentException(
            "reps cannot be less than zero, reps provided: $reps"
        )
//        synchronized(this) {
            history[workout] = reps
//        }
    }

//    @Synchronized
    operator fun inc(workout: Workout): WorkoutHistory {
        history[workout] = history[workout]!! + 1
        return WorkoutHistory(history)
    }

    /**
     * Dec operator doesn't really make sense for this
     * use-case, but implementing it for parity's sake.
     * */
//    @Synchronized
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