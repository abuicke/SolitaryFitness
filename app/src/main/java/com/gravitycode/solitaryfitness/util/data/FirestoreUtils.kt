package com.gravitycode.solitaryfitness.util.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.gravitycode.solitaryfitness.util.error.debugError
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

/**
 * Delete all documents which are found in this collection.
 *
 * Note: Any documents which don't contain fields (such as those which only contain a collection) will not
 * show up when the collection is queried. Therefore, only documents which contain fields can be deleted by
 * this function. If you have a structure such as:
 * ```
 * Collection A -> Document B -> Collection C -> Document D -> { a:1, b:2, c:3 }
 * ```
 * where only Document D actually contains any data, by deleting Document D, all of the parents will
 * automatically be removed. Therefore, [deleteDocuments] could be called on Collection C, but not on
 * Collection A in order to remove this entire structure. This is because the Firestore does not actually
 * apply a parent/child relationship, where Document D is a child of Collection A, for some ungodly reason.
 *
 * [Delete a firestore collection including sub-collections](https://stackoverflow.com/questions/67149779)
 *
 * [Firestore not listing documents with collection but no fields](https://stackoverflow.com/questions/69569496)
 * */
suspend fun CollectionReference.deleteDocuments() {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener { queryTask ->
            /**
             * TODO: I seem to be going on to use the `queryTask.result` even if `queryTask.isSuccessful` is false?
             * */
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