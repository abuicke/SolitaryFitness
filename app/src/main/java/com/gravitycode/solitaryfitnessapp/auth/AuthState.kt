package com.gravitycode.solitaryfitnessapp.auth

data class AuthState(val user: User?) {

    fun isUserSignedIn() = user != null
}