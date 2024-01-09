package com.gravitycode.solitaryfitness.test_implementations

import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.User

class TestAuthenticator(
    userId: String,
    userName: String,
    userEmail: String,
    profilePicUrl: String
) : Authenticator {

    private val user = User(
        userId,
        userName,
        userEmail,
        profilePicUrl
    )

    override suspend fun signIn() = Result.success(user)

    override suspend fun signOut() = Result.success(Unit)

    override fun isUserSignedIn() = true

    override fun getSignedInUser() = user
}