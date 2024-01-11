package com.gravitycode.solitaryfitness.app

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
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.di.DaggerActivityComponent
import com.gravitycode.solitaryfitness.log_workout.data.SyncDataService
import com.gravitycode.solitaryfitness.log_workout.data.SyncMode
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutScreen
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.util.data.createPreferencesStoreFromFile
import com.gravitycode.solitaryfitness.util.data.stringSetPreferencesKey
import com.gravitycode.solitaryfitness.util.error.debugError
import com.gravitycode.solitaryfitness.util.ui.Messenger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * TODO: Add a test that signs out on UIAutomator.
 * */
class MainActivity : ComponentActivity(), AppController {

    private companion object {

        const val TAG = "MainActivity"
    }

    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var messenger: Messenger
    @Inject lateinit var syncDataService: SyncDataService
    @Inject lateinit var repositoryFactory: WorkoutLogsRepositoryFactory
    @Inject lateinit var logWorkoutViewModel: LogWorkoutViewModel

    override val applicationScope = lifecycleScope
    override val appState = MutableSharedFlow<AppState>(replay = 1)
    private val appControllerSettings = AppControllerSettings(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerActivityComponent.builder()
            .componentActivity(this)
            .build()
            .inject(this)

        lifecycleScope.launch {
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
                    appControllerSettings.addUserToSignInHistory(user)
                    if (hasOfflineData) {
                        launchSyncOfflineDataFlow {
                            appState.emit(AppState(user))
                        }
                    } else {
                        appState.emit(AppState(user))
                    }
                }else {
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
                    val progressDialog = AlertDialog.Builder(this@MainActivity)
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

private class AppControllerSettings(activity: ComponentActivity) {

    private companion object {

        private val USERS_KEY = stringSetPreferencesKey("users")
    }

    private val preferencesStore = createPreferencesStoreFromFile(activity, "app_controller")
    private val users: MutableSet<String> = mutableSetOf()

    init {
        activity.lifecycleScope.launch(Dispatchers.IO) {
            val preferences = preferencesStore.data.first()
            val userIds: Set<String>? = preferences[USERS_KEY]

            if (userIds != null) {
                users.addAll(userIds)
            }
        }
    }

    suspend fun addUserToSignInHistory(user: User) {
        preferencesStore.edit { preferences ->
            users.add(user.id)
            preferences[USERS_KEY] = users
        }
    }

    fun hasUserPreviouslySignedIn(user: User): Boolean {
        return users.contains(user.id)
    }
}