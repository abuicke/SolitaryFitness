package com.gravitycode.solitaryfitness.test_implementations

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.log_workout.data.FirestoreWorkoutLogsRepository
import com.gravitycode.solitaryfitness.util.data.deleteDocuments

class TestFirestoreWorkoutLogsRepository(
    appController: AppController,
    private val firestore: FirebaseFirestore,
    authenticator: Authenticator
) : FirestoreWorkoutLogsRepository(
    appController,
    firestore,
    authenticator
) {

    private companion object {

        const val TEST_USER_ID = "0"
    }

    override fun users() = firestore.collection("test-users")

    @SuppressLint("VisibleForTests")
    suspend fun clearTestRecords() {
        // Can't delete a collection or a document that only contains a collection and no data fields
        // directly, need to delete everything it contains and it will automatically be removed. If all a
        // document contains is a collection, it won't even show up in a query for some genius reason, but
        // if you add a field to it it will. So in this case I need to delete all the documents contained
        // within the workout-logs subcollection and the test user will be automatically removed.
        workoutLogs(TEST_USER_ID).deleteDocuments()
    }
}