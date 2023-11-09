package com.gravitycode.solitaryfitness.app

data class AppState(
    val isUserSignedIn: Boolean = false,
//    val user: User? = null
) {
//    init {
//        if (userSignedIn && user == null) {
//            throw IllegalStateException(
//                "if AppState.userSignedIn is set to true, AppState.user cannot be null"
//            )
//        } else if (!userSignedIn && user != null) {
//            throw IllegalStateException(
//                "if AppState.userSignedIn is set to false, AppState.user cannot br non-null"
//            )
//        }
//    }
}