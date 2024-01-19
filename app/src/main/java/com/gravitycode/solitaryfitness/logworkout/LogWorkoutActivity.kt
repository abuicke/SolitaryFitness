package com.gravitycode.solitaryfitness.logworkout

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.gravitycode.solitaryfitness.R
import com.gravitycode.solitaryfitness.app.AppState
import com.gravitycode.solitaryfitness.app.FlowLauncher
import com.gravitycode.solitaryfitness.app.SolitaryFitnessApp
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.logworkout.data.SyncDataService
import com.gravitycode.solitaryfitness.logworkout.data.SyncMode
import com.gravitycode.solitaryfitness.logworkout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutScreen
import com.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.data.stringSetPreferencesKey
import com.gravitycode.solitaryfitness.util.error.debugError
import com.gravitycode.solitaryfitness.util.ui.Messenger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 *
 *
 *
 *
 * TODO: Test internet connection stuff next
 *
 *
 *
 *
 *
 *
 *
 * TODO: Should make an abstract activity that handles ActivityComponent stuff the same way Application
 *  handles the Application component stuff?
 *
 * TODO: Add a test that signs out on UIAutomator.
 * TODO: Figure out DataStore issue.
 * TODO: Write test to test UI with firebase that doesn't choosing an account to sign in with.
 * TODO: Use Mockito for Firestore: https://softwareengineering.stackexchange.com/questions/450508
 * TODO: Test no internet connection
 * TODO: Need error handling and return [Result] everywhere preferences store is accessed.
 * TODO: Check to see of there are other locations where I can use runCatching to return [Result]s
 * TODO: Write test for [AppControllerSettings]
 * TODO: Are there any places where it would be more profitable to us async/await? (Anywhere a result is
 *  waited for, what about logging in and out?)
 * */
class LogWorkoutActivity : ComponentActivity(), FlowLauncher {

    /**
     *
     *
     * TODO: Test internet connection stuff next
     *
     *
     * */

    private companion object {

        const val TAG = "MainActivity"
    }

    @Inject lateinit var applicationScope: CoroutineScope
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var messenger: Messenger
    @Inject lateinit var syncDataService: SyncDataService
    @Inject lateinit var repositoryFactory: WorkoutLogsRepositoryFactory
    @Inject lateinit var logWorkoutViewModel: LogWorkoutViewModel

    private val appState = MutableSharedFlow<AppState>(replay = 1)

    private lateinit var appControllerSettings: AppControllerSettings

    /**
     *
     *
     *
     * TODO: Need to make sure components are garbage collected when no longer used.
     *
     *
     *
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as SolitaryFitnessApp
        app.activityComponent(this, appState, this)
            .logWorkoutComponentBuilder()
            .build()
            .inject(this)

        lifecycleScope.launch {
            appControllerSettings = AppControllerSettings.getInstance(dataStoreManager)
        }

        applicationScope.launch {
            val currentUser = authenticator.getSignedInUser()
            appState.emit(AppState(currentUser))
        }

        setContent {
            SolitaryFitnessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LogWorkoutScreen(logWorkoutViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            applicationScope.cancel("MainActivity destroyed")
        }
    }

    override fun launchSignInFlow() {
        if (authenticator.isUserSignedIn()) {
            val currentUser = authenticator.getSignedInUser()!!
            val name = currentUser.name ?: currentUser.email ?: currentUser.id
            messenger.toast("You are already signed in as $name")
            debugError("user is already signed in as $name")
            return
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                authenticator.signIn()
            }

            val offlineRepository = repositoryFactory.getOfflineRepository()
            val firstRecord = offlineRepository.metaData.getRecords().firstOrNull()
            val hasOfflineData = firstRecord != null

            if (result.isSuccess) {
                val user = result.getOrNull()!!
                val hasUserPreviouslySignedIn = appControllerSettings.hasUserPreviouslySignedIn(user)
                if (!hasUserPreviouslySignedIn) {
                    val historyResult = appControllerSettings.addUserToSignInHistory(user)
                    if (historyResult.isSuccess) {
                        Log.d(TAG, "successfully added user ${user.email} to sign in history")
                    } else {
                        debugError("failed to add user ${user.email} to sign in history")
                    }
                    if (hasOfflineData) {
                        launchSyncOfflineDataFlow {
                            appState.emit(AppState(user))
                        }
                    } else {
                        appState.emit(AppState(user))
                    }
                } else {
                    appState.emit(AppState(user))
                }
                messenger.toast("Signed in: ${user.email}")
                Log.d(TAG, "signed in as user: $user")
            } else {
                messenger.toast("Failed to sign in")
                debugError("Sign in failed", result)
            }
        }
    }

    override fun launchSignOutFlow() {
        check(authenticator.isUserSignedIn()) { "no user is signed in" }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                authenticator.signOut()
            }

            if (result.isSuccess) {
                appState.emit(AppState(null))
                messenger.toast("Signed out")
                Log.v(TAG, "signed out")
            } else {
                messenger.toast("Failed to sign out")
                debugError("Sign out failed", result)
            }
        }
    }

    override fun launchSyncOfflineDataFlow() = launchSyncOfflineDataFlow(null)

    private fun launchSyncOfflineDataFlow(onComplete: (suspend () -> (Unit))?) {
        AlertDialog.Builder(this)
            .setTitle(R.string.transfer_dialog_title)
            .setMessage(R.string.transfer_dialog_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    val progressDialog = AlertDialog.Builder(this@LogWorkoutActivity)
                        .setView(R.layout.sync_progress_dialog)
                        .setCancelable(false)
                        .setOnDismissListener {
                            messenger.toast("Sync complete")
                        }
                        .show()

                    try {
                        withContext(Dispatchers.IO) {
                            syncDataService.sync(SyncMode.OVERWRITE).collect { resultOf ->
                                if (resultOf.isFailure) {
                                    withContext(Dispatchers.Main) {
                                        messenger.toast("Sync failed for ${resultOf.subject}")
                                    }
                                } else {
                                    Log.v(TAG, "successfully synced ${resultOf.subject}")
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        messenger.toast("Sync failed...")
                        debugError("sync data service failed: ${t.message}", t)
                    } finally {
                        progressDialog.dismiss()
                        onComplete?.invoke()
                    }
                }
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                lifecycleScope.launch {
                    dialog.dismiss()
                    onComplete?.invoke()
                }
            }
            .show()
    }
}

class AppControllerSettings private constructor(dataStoreManager: DataStoreManager) {

    companion object {

        private val USERS_KEY = stringSetPreferencesKey("users")

        private val mutex = Mutex()

        private var instance: AppControllerSettings? = null

        /**
         * Return the singleton instance of [AppControllerSettings]
         * */
        suspend fun getInstance(dataStoreManager: DataStoreManager): AppControllerSettings {
            return instance ?: mutex.withLock {
                instance ?: AppControllerSettings(dataStoreManager).apply {
                    try {
                        val preferences = withContext(Dispatchers.IO) {
                            preferencesStore.data.first()
                        }
                        val userIds: Set<String>? = preferences[USERS_KEY]
                        if (userIds != null) {
                            users.addAll(userIds)
                        }
                    } catch (ioe: IOException) {
                        debugError("failed to read app controller settings from preferences store", ioe)
                    }

                    instance = this
                }
            }
        }
    }

    private val preferencesStore = dataStoreManager.datastore("app_controller")
    private val users: MutableSet<String> = mutableSetOf()

    suspend fun addUserToSignInHistory(user: User): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                preferencesStore.edit { preferences ->
                    users.add(user.id)
                    preferences[USERS_KEY] = users
                }
            }
        }
    }

    fun hasUserPreviouslySignedIn(user: User): Boolean {
        return users.contains(user.id)
    }
}