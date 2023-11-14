package com.gravitycode.solitaryfitness.util.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.firestore.proto.TargetGlobal
import com.gravitycode.solitaryfitness.track_reps.data.TestFirestoreWorkoutHistoryRepo
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.test.testError
import java.util.Arrays
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "FirestoreUtils"

fun firestoreSettings(persistentCacheSizeMb: Int): FirebaseFirestoreSettings {
    require(persistentCacheSizeMb >= 1) { "cache size cannot be less than 1MB" }

    return firestoreSettings {
        setLocalCacheSettings(persistentCacheSettings {
            val sizeBytes = megabytesToBytes(persistentCacheSizeMb)
            setSizeBytes(sizeBytes)
        })
    }
}

suspend fun CollectionReference.delete() {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener { queryTask ->
            if (!queryTask.isSuccessful) debugError("query firestore task failed", queryTask.exception)
            val query: QuerySnapshot = queryTask.result
            val documents = query.documents
            val completed = Array(documents.size) { false }
            Log.v(TAG, "number of documents to delete = ${documents.size}")
            if (query.documents.isEmpty()) {
                continuation.resume(Unit)
            } else {
                for (i in 0 until query.documents.size) {
                    val document = query.documents[i]
                    Log.d(TAG, "deleting document ${document.id}")
                    document.reference.delete().addOnCompleteListener { deleteTask ->
                        completed[i] = true
                        if (deleteTask.isSuccessful) {
                            Log.d(TAG, "deleted document successfully")
                        } else {
                            Log.e(TAG, "failed to delete document", deleteTask.exception)
                        }

                        if (completed.reduce { acc, next -> acc && next }) {
                            Log.d(TAG, "all documents have been deleted")
                            continuation.resume(Unit)
                        }
                    }
                }
            }
        }
    }
}