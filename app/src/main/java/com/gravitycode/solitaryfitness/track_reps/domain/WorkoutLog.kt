package com.gravitycode.solitaryfitness.track_reps.domain

import com.google.common.collect.ImmutableMap
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.util.EnumMap

class WorkoutLog(
    log: Map<Workout, Int> = EnumMap(Workout::class.java)
) {

    private val log: EnumMap<Workout, Int> = EnumMap(log)

    init {
        for (workout in Workout.values()) {
            val reps = this.log[workout]
            if (reps == null) {
                this.log[workout] = 0
            } else if (reps < 0) {
                throw IllegalArgumentException(
                    "cannot assign negative reps value: $workout = $reps"
                )
            }
        }
    }

    operator fun get(workout: Workout): Int {
        return log[workout]!!
    }

    operator fun set(workout: Workout, reps: Int) {
        require(reps >= 0) { "reps cannot be less than zero, reps provided: $reps" }
        log[workout] = reps
    }

    fun toMap(): Map<Workout, Int> = ImmutableMap.copyOf(log)

    override fun equals(other: Any?) =
        other is WorkoutLog && log == other.log

    override fun hashCode() = log.hashCode()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('{')
        builder.append('\n')
        for (workout in Workout.values()) {
            builder.append(
                "\t${workout.string}: ${log[workout]}\n"
            )
        }
        builder.append('}')

        return builder.toString()
    }
}