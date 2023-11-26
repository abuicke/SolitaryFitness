package com.gravitycode.solitaryfitness.log_workout.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * If you are using [DocumentReference.set] to update document fields then be careful, it kills non-updated
 * fields in the document. eg. I had 6 fields in a document and I updated only 4 fields then `set()` method
 * removed 2 fields and their information from that document on firestore.
 *
 * (How to add Document with Custom ID to firestore)[https://stackoverflow.com/a/48544954/4596649]
 * */
open class FirestoreWorkoutLogsRepository(
    private val firestore: FirebaseFirestore,
    private val authenticator: Authenticator
) : WorkoutLogsRepository {

    private companion object {
        const val TAG = "FirestoreWorkoutHistoryRepo"
    }

    override val metaData: MetaData<LocalDate>
        get() = TODO("Not yet implemented")

    final override suspend fun readWorkoutLog(date: LocalDate): Result<WorkoutLog?> {
        return suspendCoroutine { continuation ->
            workoutLog(date).get()
                .addOnSuccessListener { document ->
                    val data = document.data
                    Log.v(TAG, "readWorkoutLog($date): $data")

                    val result = if (data != null) {
                        val map = HashMap<Workout, Int>(Workout.values().size)
                        for (entry: Map.Entry<String, Any> in data) {
                            val workout = Workout.fromString(entry.key)!!
                            map[workout] = entry.value.toString().toInt()
                        }
                        Result.success(WorkoutLog.from(map))
                    } else {
                        Result.success(null)
                    }

                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    Log.e(TAG, "failed to read workout history from firestore", e)
                    val result = Result.failure<WorkoutLog>(e)
                    continuation.resume(result)
                }
        }
    }

    final override suspend fun writeWorkoutLog(date: LocalDate, log: WorkoutLog): Result<Unit> {
        return suspendCoroutine { continuation ->
            val serializableMap = log.toMap().mapKeys { it.key.string }

            workoutLog(date).set(serializableMap)
                .addOnSuccessListener {
                    Log.d(TAG, "successfully wrote workout logs to firestore")
                    val result = Result.success(Unit)
                    continuation.resume(result)
                }.addOnFailureListener { e ->
                    debugError("error writing workout logs to firestore")
                    val result = Result.failure<Unit>(e)
                    continuation.resume(result)
                }
        }
    }

    final override suspend fun updateWorkoutLog(
        date: LocalDate,
        workout: Workout,
        reps: Int
    ): Result<Unit> {
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

    final override suspend fun deleteWorkoutLog(date: LocalDate): Result<Unit> {
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

    /**
     * @return the top-level collection which contains all users,
     * can be overridden by a child class to change where users are stored.
     * */
    protected open fun users() = firestore.collection("users")

    /**
     * @param[userId] The user's ID
     *
     * @return a collection containing all the workout logs for the specified user
     * */
    protected fun workoutLogs(userId: String): CollectionReference {
        return users().document(userId).collection("workout-logs")
    }

    private fun workoutLog(date: LocalDate): DocumentReference {
        val currentUser = authenticator.getSignedInUser()!!
        return workoutLogs(currentUser.id).document(date.toString())
    }
}