package com.gravitycode.solitaryfitness.app

open class AppEvent<T : AppEvent<T>> {

    object SignIn: AppEvent<Nothing>()
    object SignOut: AppEvent<Nothing>()
}







