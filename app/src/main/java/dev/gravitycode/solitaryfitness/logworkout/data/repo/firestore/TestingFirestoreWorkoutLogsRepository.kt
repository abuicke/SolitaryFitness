package dev.gravitycode.solitaryfitness.logworkout.data.repo.firestore

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import dev.gravitycode.solitaryfitness.auth.Authenticator
import dev.gravitycode.solitaryfitness.auth.User
import dev.gravitycode.solitaryfitness.util.deleteDocuments
import kotlinx.coroutines.CoroutineScope

class TestingFirestoreWorkoutLogsRepository(
    applicationScope: CoroutineScope,
    user: User,
    private val firestore: FirebaseFirestore
) : FirestoreWorkoutLogsRepository(applicationScope, TestAuthenticator(user)) {

    override fun users() = firestore.collection("test-users")

    /**
     * Delete all data for the current user.
     * */
    @SuppressLint("VisibleForTests")
    suspend fun deleteAll() {
        // Can't delete a collection or a document that only contains a collection and no data fields
        // directly, need to delete everything it contains and it will automatically be removed. If all a
        // document contains is a collection, it won't even show up in a query for some genius reason, but
        // if you add a field to it it will. So in this case I need to delete all the documents contained
        // within the workout-logs subcollection and the test user will be automatically removed.
        workoutLogs().deleteDocuments()
    }
}

/**
 * Firestore authentication is disabled for the `test-users` collection.
 * */
private class TestAuthenticator(private val user: User) : Authenticator {

    override suspend fun signIn() = Result.success(user)

    override suspend fun signOut() = Result.success(Unit)

    override fun isUserSignedIn() = true

    override fun getSignedInUser() = user
}