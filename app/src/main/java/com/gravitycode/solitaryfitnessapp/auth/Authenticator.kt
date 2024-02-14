package com.gravitycode.solitaryfitnessapp.auth

interface Authenticator {

    suspend fun signIn(): Result<User>

    suspend fun signOut(): Result<Unit>

    fun isUserSignedIn(): Boolean

    fun getSignedInUser(): User?
}