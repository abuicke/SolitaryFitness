package com.gravitycode.solitaryfitness.auth

import com.google.firebase.auth.FirebaseUser

data class User(
    val email: String
) {

    constructor(firebaseUser: FirebaseUser): this(firebaseUser.email!!)
}