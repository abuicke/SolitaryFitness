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
            workoutLogs(date).get()
                .addOnSuccessListener { document ->
                    Log.v(TAG, "readWorkoutHistory: ${document.id} => ${document.data}")
                    val workoutLog = WorkoutLog(
                        document.data!!.entries.associate { entry: MutableMap.MutableEntry<String, Any> ->
                            Workout.fromString(entry.key)!! to entry.toString().toInt()
                        }
                    )
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

            workoutLogs(date).set(serializableMap)
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

    private fun workoutLogs(date: LocalDate): DocumentReference {
        return firestore.collection("users")
            .document(currentUser.id)
            .collection("workout-logs")
            .document(date.toString())
    }
}