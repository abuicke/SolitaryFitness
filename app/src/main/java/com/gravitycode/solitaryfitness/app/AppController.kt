package com.gravitycode.solitaryfitness.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface AppController {

    val applicationScope: CoroutineScope

    /**
     * Will always replay the last emitted [AppState] for new subscribers.
     * */
    val appState: SharedFlow<AppState>

    fun requestSignIn()

    fun requestSignOut()

    fun launchTransferDataFlow()
}