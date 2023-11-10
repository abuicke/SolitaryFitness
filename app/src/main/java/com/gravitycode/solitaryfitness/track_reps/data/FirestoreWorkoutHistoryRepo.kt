package com.gravitycode.solitaryfitness.track_reps.data

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.MainActivity
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.track_reps.domain.WorkoutLog
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreWorkoutHistoryRepo(
    private val firestore: FirebaseFirestore,
    authenticator: Authenticator
) : WorkoutHistoryRepo {

    companion object {
        const val TAG = "FirestoreWorkoutHistoryRepo"
    }

    private val currentUser: User = authenticator.getSignedInUser()!!

    override suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog> {
        return suspendCoroutine { continuation ->
            workoutLog(date).get()
                .addOnSuccessListener { document ->
                    val data = document.data
                    Log.v(TAG, "readWorkoutLog: $data")

                    val workoutLog = WorkoutLog()
                    if (data != null) {
                        for (entry: Map.Entry<String, Any> in data) {
                            val workout = Workout.fromString(entry.key)!!
                            workoutLog[workout] = entry.value.toString().toInt()
                        }
                    }

                    val result = Result.success(workoutLog)
                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    Log.e(TAG, "failed to read workout history from firestore", e)
                    val result = Result.failure<WorkoutLog>(e)
                    continuation.resume(result)
                }
        }
    }

    override suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit> {
        return suspendCoroutine { continuation ->
            val serializableMap = log.toMap().mapKeys { it.key.string }

            workoutLog(date).set(serializableMap)
                .addOnSuccessListener {
                    Log.d(MainActivity.TAG, "successfully wrote workout logs to firestore")
                    val result = Result.success(Unit)
                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    debugError("error writing workout logs to firestore")
                    val result = Result.failure<Unit>(e)
                    continuation.resume(result)
                }
        }
    }

    override suspend fun updateWorkoutLog(date: LocalDate, workout: Workout, reps: Int): Result<Unit> {
        return suspendCoroutine { continuation ->
            require(reps >= 0) { "reps cannot be less than zero, reps provided: $reps" }
            workoutLog(date).update(workout.string, reps)
                .addOnSuccessListener {
                    val result = Result.success(Unit)
                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    Log.e(TAG, "failed to update workout log: update($date, $workout, $reps)", e)
                    val result = Result.failure<Unit>(e)
                    continuation.resume(result)
                }
        }
    }

    override suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit> {
        return suspendCoroutine { continuation ->
            workoutLog(date).delete()
                .addOnSuccessListener {
                    val result = Result.success(Unit)
                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    Log.e(TAG, "failed to delete workout log for $date", e)
                    val result = Result.failure<Unit>(e)
                    continuation.resume(result)
                }
        }
    }

    private fun workoutLog(date: LocalDate): DocumentReference {
        return firestore.collection("users")
            .document(currentUser.id)
            .collection("workout-logs")
            .document(date.toString())
    }
}