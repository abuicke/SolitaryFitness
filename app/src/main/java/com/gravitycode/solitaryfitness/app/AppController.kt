package com.gravitycode.solitaryfitness.app

import kotlinx.coroutines.flow.StateFlow

interface AppController {

    val appState: StateFlow<AppState>

    fun requestSignIn()

    fun requestSignOut()
}