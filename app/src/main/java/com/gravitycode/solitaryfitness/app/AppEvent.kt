package com.gravitycode.solitaryfitness.app

sealed class AppEvent {

    object SignIn : AppEvent()
    object SignOut : AppEvent()
}
