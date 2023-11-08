package com.gravitycode.solitaryfitness.auth

import com.google.firebase.auth.FirebaseUser

class User(private val firebaseUser: FirebaseUser) {

    override fun toString() = firebaseUser.toString()

    override fun equals(other: Any?) =
        other is User && firebaseUser == other.firebaseUser

    override fun hashCode() = firebaseUser.hashCode()
}