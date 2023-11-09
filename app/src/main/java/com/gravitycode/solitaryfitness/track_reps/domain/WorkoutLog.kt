package com.gravitycode.solitaryfitness.track_reps.domain

import androidx.annotation.IntRange
import com.google.common.collect.ImmutableMap
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.util.EnumMap

class WorkoutLog(
    history: Map<Workout, Int> = EnumMap(Workout::class.java)
) {

    val history: EnumMap<Workout, Int> = EnumMap(history)

    init {
        for (workout in Workout.values()) {
            val reps = this.history[workout]
            if (reps == null) {
                this.history[workout] = 0
            } else if (reps < 0) {
                throw IllegalArgumentException(
                    "cannot assign negative reps value: $workout = $reps"
                )
            }
        }
    }

    operator fun get(workout: Workout): Int {
        return history[workout]!!
    }

    operator fun set(workout: Workout, @IntRange(from = 0) reps: Int) {
        require(reps >= 0) { "reps cannot be less than zero, reps provided: $reps" }
        history[workout] = reps
    }

    fun toMap(): Map<Workout, Int> = ImmutableMap.copyOf(history)

    override fun equals(other: Any?) =
        other is WorkoutLog && history == other.history

    override fun hashCode() = history.hashCode()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('{')
        builder.append('\n')
        for (workout in Workout.values()) {
            builder.append(
                "\t${workout.prettyString}: ${history[workout]}\n"
            )
        }
        builder.append('}')

        return builder.toString()
    }
}