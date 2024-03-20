package dev.gravitycode.solitaryfitness.app

interface FlowLauncher {

    fun launchSignInFlow()

    fun launchSignOutFlow()

    fun launchSyncOfflineDataFlow()
}