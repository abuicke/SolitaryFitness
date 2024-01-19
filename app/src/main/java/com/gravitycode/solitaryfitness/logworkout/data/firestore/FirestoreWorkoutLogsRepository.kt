package com.gravitycode.solitaryfitness.logworkout.data.firestore

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.logworkout.data.WorkoutLogsRepository
import com.gravitycode.solitaryfitness.logworkout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.logworkout.util.Workout
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
 * NOTE: "If you are using [DocumentReference.set] to update document fields then be careful, it kills
 * non-updated fields in the document. eg. I had 6 fields in a document and I updated only 4 fields then
 * `set()` method removed 2 fields and their information from that document on firestore."
 *
 * (How to add Document with Custom ID to firestore)[https://stackoverflow.com/a/48544954/4596649]
 * */
sealed class FirestoreWorkoutLogsRepository(
    applicationScope: CoroutineScope,
    private val authenticator: Authenticator
) : WorkoutLogsRepository {

    private companion object {

        const val TAG = "FirestoreWorkoutHistoryRepo"
    }

//    /**
//     * ID string for the root collection (the users collection), which contains all the data this repository
//     * will retrieve. Override this property in a class to specify an ID for this collection. This
//     * allows for different root collections to be used depending on the circumstances, e.g. production,
//     * debugging, testing etc.
//     * */
//    protected abstract val usersCollectionId: String

    private val _metaData = FirestoreMetaData(applicationScope)
    final override val metaData: MetaData<LocalDate> = _metaData

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
     * @return the top-level collection, which contains all users (and their data)
     * */
    protected abstract fun users(): CollectionReference

    /**
     * @param[userId] The user's ID
     *
     * @return a collection containing all the data (including workout logs) for the specified user
     * */
    private fun workoutLogs(userId: String) = users().document(userId).collection("workout-logs")

    /**
     * @return a collection containing all the data (including workout logs) for the current user
     *
     * @throws NullPointerException if no user is currently signed in
     * */
    @VisibleForTesting
    protected fun workoutLogs() = workoutLogs(authenticator.getSignedInUser()!!.id)

    /**
     * @param[date] The date of the workout logs
     *
     * @return a document containing the workout logs for the specified date (for the current user)
     * */
    private fun workoutLog(date: LocalDate) = workoutLogs().document(date.toString())

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