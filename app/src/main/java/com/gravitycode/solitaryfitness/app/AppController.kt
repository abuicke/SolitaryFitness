package com.gravitycode.solitaryfitness.app

import kotlinx.coroutines.flow.SharedFlow

interface AppController {

    val appState: SharedFlow<AppState>

    fun requestSignIn()

    fun requestSignOut()

    fun launchTransferDataFlow()
}