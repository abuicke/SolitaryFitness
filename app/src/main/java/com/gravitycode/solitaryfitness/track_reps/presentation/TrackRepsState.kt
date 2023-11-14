package com.gravitycode.solitaryfitness.track_reps.presentation

import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import java.time.LocalDate

class TrackRepsState(
    val date: LocalDate,
    val log: WorkoutLog
) {

    /**
     * TODO: I thought the point of the data class was that it generates exactly these functions?
     *  Need to verify and remove.
     * */

//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//
//        return other is TrackRepsState &&
//                date == other.date &&
//                log == other.log
//    }
//
//    override fun hashCode(): Int {
//        var result = date.hashCode()
//        result = 31 * result + log.hashCode()
//        return result
//    }
}