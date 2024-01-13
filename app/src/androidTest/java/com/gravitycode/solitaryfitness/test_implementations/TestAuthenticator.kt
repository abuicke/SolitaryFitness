package com.gravitycode.solitaryfitness.test_implementations

import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.User

class TestAuthenticator(private val user: User) : Authenticator {

    override suspend fun signIn() = Result.success(user)

    override suspend fun signOut() = Result.success(Unit)

    override fun isUserSignedIn() = true

    override fun getSignedInUser() = user
}