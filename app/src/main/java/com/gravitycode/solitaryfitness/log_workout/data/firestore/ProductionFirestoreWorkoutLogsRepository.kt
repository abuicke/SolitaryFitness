package com.gravitycode.solitaryfitness.log_workout.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.auth.Authenticator
import kotlinx.coroutines.CoroutineScope

class ProductionFirestoreWorkoutLogsRepository(
    applicationScope: CoroutineScope,
    authenticator: Authenticator,
    private val firestore: FirebaseFirestore,
) : FirestoreWorkoutLogsRepository(applicationScope, authenticator) {

    override fun users() = firestore.collection("users")
}