package dev.gravitycode.solitaryfitness.logworkout.data.repo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import dev.gravitycode.solitaryfitness.auth.Authenticator
import kotlinx.coroutines.CoroutineScope

class DebugFirestoreWorkoutLogsRepository(
    applicationScope: CoroutineScope,
    authenticator: Authenticator,
    private val firestore: FirebaseFirestore,
) : FirestoreWorkoutLogsRepository(applicationScope, authenticator) {

    override fun users() = firestore.collection("debug-users")
}