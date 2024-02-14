package com.gravitycode.solitaryfitnessapp.app

interface FlowLauncher {

    fun launchSignInFlow()

    fun launchSignOutFlow()

    fun launchSyncOfflineDataFlow()
}