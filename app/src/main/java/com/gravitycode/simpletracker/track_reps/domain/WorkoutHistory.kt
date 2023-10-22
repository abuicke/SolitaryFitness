package com.gravitycode.simpletracker.track_reps.domain

import androidx.annotation.IntRange
import com.google.common.base.Preconditions.checkArgument
import com.google.common.collect.ImmutableMap
import com.gravitycode.simpletracker.track_reps.util.Workout
import java.util.EnumMap

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
        checkArgument(reps >= 0, "reps cannot be less than zero, reps provided: $reps")
        history[workout] = reps
    }

    fun toMap(): Map<Workout, Int> = ImmutableMap.copyOf(history)

    override fun equals(other: Any?) =
        other is WorkoutHistory && history == other.history

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