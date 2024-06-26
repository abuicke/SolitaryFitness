package dev.gravitycode.solitaryfitness.auth

data class AuthState(val user: User?) {

    fun isUserSignedIn() = user != null
}