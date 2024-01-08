package com.gravitycode.solitaryfitness.app

sealed class AppEvent {

    object SignIn : AppEvent() {
        override fun toString() = "AppEvent.SignIn"
    }

    object SignOut : AppEvent() {
        override fun toString() = "AppEvent.SignOut"
    }
}
