package com.gravitycode.solitaryfitness.auth

interface Authenticator {

    fun signIn()

    fun signOut()

    fun isUserSignedIn(): Boolean

    fun getSignedInUser(): User?
}