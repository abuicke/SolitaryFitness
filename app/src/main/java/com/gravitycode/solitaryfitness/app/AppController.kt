package com.gravitycode.solitaryfitness.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import androidx.lifecycle.lifecycleScope

/**
 * In the event that this interface needs to be implemented by a stand-alone class, and not an activity,
 * look into [Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle)
 * */
interface AppController {

    /**
     * Bound to [Dispatchers.Main] by default. Exists for the lifetime of the application.
     *
     * Warning: Do not use [lifecycleScope] for this property, as this will be destroyed between screen rotations.
     * */
    val applicationScope: CoroutineScope

    /**
     * Will always replay the last emitted [AppState] for new subscribers.
     * */
    val appState: SharedFlow<AppState>

    fun launchSignInFlow()

    fun launchSignOutFlow()

    fun launchSyncOfflineDataFlow()
}