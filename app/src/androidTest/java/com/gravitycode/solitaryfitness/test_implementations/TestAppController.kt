package com.gravitycode.solitaryfitness.test_implementations

import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.app.AppState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope

class TestAppController : AppController {

    override val applicationScope = TestScope()

    override val appState = MutableSharedFlow<AppState>()

    override fun launchSignInFlow() {
        throw NotImplementedError()
    }

    override fun launchSignOutFlow() {
        throw NotImplementedError()
    }

    override fun launchSyncOfflineDataFlow() {
        throw NotImplementedError()
    }
}