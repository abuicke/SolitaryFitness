package com.gravitycode.solitaryfitness.log_workout.data

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.data.MetaData
import com.gravitycode.solitaryfitness.util.error.debugError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
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
    appController: AppController,
    private val firestore: FirebaseFirestore,
    private val authenticator: Authenticator
) : WorkoutLogsRepository {

    private companion object {

        const val TAG = "FirestoreWorkoutHistoryRepo"
    }

    private val _metaData = FirestoreMetaData(appController.applicationScope)
    /**
     * TODO: Is this required to be open for testing or should it also be made final?
     * */
    override val metaData: MetaData<LocalDate> = _metaData

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
                    _metaData.add(date)
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
                    _metaData.remove(date)
                    val result = Result.failure<Unit>(e)
                    continuation.resume(result)
                }
        }
    }

    /**
     * @return the top-level collection which contains all users,
     * can be overridden by a child class to change where users are stored.
     * */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected open fun users() = firestore.collection("users")

    /**
     * @param[userId] The user's ID
     *
     * @return a collection containing all the workout logs for the specified user
     * */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected fun workoutLogs(userId: String): CollectionReference {
        return users().document(userId).collection("workout-logs")
    }

    private fun workoutLogs() = workoutLogs(authenticator.getSignedInUser()!!.id)

    private fun workoutLog(date: LocalDate): DocumentReference {
        return workoutLogs().document(date.toString())
    }

    private inner class FirestoreMetaData(scope: CoroutineScope) : MetaData<LocalDate> {

        /*
        * Using HashSet as long as the order of keys does not need to be maintained.
        * Switch implementation to LinkedHashSet if the contract with MetaData changes.
        * */
        private val records = HashSet<LocalDate>()

        init {
            scope.launch(Dispatchers.IO) {
                workoutLogs().get()
                    .addOnSuccessListener { documents ->
                        records.addAll(documents.map { document -> LocalDate.parse(document.id) })
                    }
                    .addOnFailureListener { exception ->
                        debugError("failed to retrieve document IDs", exception)
                    }
            }
        }

        override fun getRecords() = records.asFlow()

        override fun containsRecord(key: LocalDate) = records.contains(key)

        fun add(date: LocalDate) = records.add(date)

        fun remove(date: LocalDate) = records.remove(date)
    }
}