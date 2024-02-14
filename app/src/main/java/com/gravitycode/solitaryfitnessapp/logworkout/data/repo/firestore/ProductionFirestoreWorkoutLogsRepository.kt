package com.gravitycode.solitaryfitnessapp.logworkout.data.repo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitnessapp.auth.Authenticator
import kotlinx.coroutines.CoroutineScope

class ProductionFirestoreWorkoutLogsRepository(
    applicationScope: CoroutineScope,
    authenticator: Authenticator,
    private val firestore: FirebaseFirestore,
) : FirestoreWorkoutLogsRepository(applicationScope, authenticator) {

    override fun users() = firestore.collection("users")
}