package com.gravitycode.solitaryfitness.app

import com.gravitycode.solitaryfitness.auth.User

data class AppState(val user: User?) {

    fun isUserSignedIn() = user != null
}