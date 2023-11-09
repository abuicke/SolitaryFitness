package com.gravitycode.solitaryfitness.auth

interface Authenticator {

    suspend fun signIn(): Result<User>

    fun signOut()

    fun isUserSignedIn(): Boolean

    fun getSignedInUser(): User?
}