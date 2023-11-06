package com.gravitycode.solitaryfitness.track_reps.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutHistory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import java.time.LocalDate

class FirestoreWorkoutHistoryRepo(
    private val firestore: FirebaseFirestore
) : WorkoutHistoryRepo {

    @OptIn(FlowPreview::class)
    override suspend fun readWorkoutHistory(date: LocalDate): Flow<WorkoutHistory> {
        return firestore.collection("workout_history")
            .dataObjects<WorkoutHistory>()
            .flatMapConcat { it.asFlow() }
    }

    override suspend fun writeWorkoutHistory(
        date: LocalDate,
        history: WorkoutHistory
    ): Result<Unit> {
        firestore.collection("workout_history")
            .add(history)
            .addOnSuccessListener { documentReference ->
                Log.d("mo", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("mo", "Error adding document", e)
            }

        return Result.failure(Exception())
    }
}