package dev.gravitycode.solitaryfitness.logworkout

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.gravitycode.caimito.kotlin.core.Log
import com.gravitycode.caimito.kotlin.data.stringSetPreferencesKey
import com.gravitycode.caimito.kotlin.net.InternetMonitor
import com.gravitycode.caimito.kotlin.core.error
import com.gravitycode.caimito.kotlin.ui.Messenger
import com.gravitycode.caimito.kotlin.ui.android.Snackbar
import com.gravitycode.caimito.kotlin.ui.android.ToastDuration
import com.gravitycode.caimito.kotlin.ui.android.Toaster
import dev.gravitycode.solitaryfitness.R
import dev.gravitycode.solitaryfitness.app.FlowLauncher
import dev.gravitycode.solitaryfitness.app.SolitaryFitnessApp
import dev.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import dev.gravitycode.solitaryfitness.auth.AuthState
import dev.gravitycode.solitaryfitness.auth.AuthenticationObservable
import dev.gravitycode.solitaryfitness.auth.Authenticator
import dev.gravitycode.solitaryfitness.auth.User
import dev.gravitycode.solitaryfitness.logworkout.data.repo.WorkoutLogsRepositoryFactory
import dev.gravitycode.solitaryfitness.logworkout.data.sync.SyncDataService
import dev.gravitycode.solitaryfitness.logworkout.data.sync.SyncMode
import dev.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutScreen
import dev.gravitycode.solitaryfitness.logworkout.presentation.LogWorkoutViewModel
import dev.gravitycode.solitaryfitness.util.data.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.annotation.concurrent.NotThreadSafe
import javax.inject.Inject

/**
 * TODO: There's a lot of logic that would be fragile to repeat across multiple activities, i.e. setting up
 *  the authentication state, launching flows, providing [Messenger] functionality, specifically Snackbar,
 *  but there is also an opportunity to provide a convenient `toast` function in the child activities.
 *
 * TODO: Need to gracefully recover from exceptions anywhere they're thrown. Look through the source code
 *  and look for anywhere there's an explicit `throw` and change this.
 *
 * TODO: `onEvent(DateSelected)` still being called 3 times
 * */
class LogWorkoutActivity : ComponentActivity(), Messenger, AuthenticationObservable, FlowLauncher {

    private companion object {

        const val TAG = "MainActivity"
    }

    @Inject lateinit var applicationScope: CoroutineScope
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var internetMonitor: InternetMonitor
    @Inject lateinit var syncDataService: SyncDataService
    @Inject lateinit var repositoryFactory: WorkoutLogsRepositoryFactory
    @Inject lateinit var logWorkoutViewModel: LogWorkoutViewModel

    override val authState = MutableSharedFlow<AuthState>(1)

    private val toaster = Toaster.create(this)
    private val snackbar = mutableStateOf<Snackbar?>(null)

    private lateinit var appControllerSettings: AppControllerSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as SolitaryFitnessApp
        app.activityComponent(this)
            .logWorkoutComponentBuilder()
            .build()
            .inject(this)

        lifecycleScope.launch {
            val currentUser = authenticator.getSignedInUser()
            authState.emit(AuthState(currentUser))

            appControllerSettings = AppControllerSettings.getInstance(dataStoreManager)

            setContent {
                SolitaryFitnessTheme {
                    SnackbarHost {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LogWorkoutScreen(logWorkoutViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun showToast(message: String, duration: ToastDuration) {
        toaster.toast(message, duration)
    }

    override fun showSnackbar(snackbar: Snackbar) {
        this.snackbar.value = snackbar
    }

    override fun showSnackbar(message: String, duration: SnackbarDuration) {
        showSnackbar(Snackbar(message, duration))
    }

    override fun launchSignInFlow() {
        if (authenticator.isUserSignedIn()) {
            val currentUser = authenticator.getSignedInUser()!!
            val name = currentUser.name ?: currentUser.email ?: currentUser.id
            return error("user is already signed in as $name") {
                showToast("You are already signed in as $name")
            }
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
                        Log.i(TAG, "successfully added user ${user.email} to sign in history")
                    } else {
                        error("failed to add user ${user.email} to sign in history")
                    }
                    if (hasOfflineData) {
                        launchSyncOfflineDataFlow {
                            authState.emit(AuthState(user))
                        }
                    } else {
                        authState.emit(AuthState(user))
                    }
                } else {
                    authState.emit(AuthState(user))
                }
                showToast("Signed in: ${user.email}")
                Log.i(TAG, "signed in as user: $user")
            } else {
                error("Sign in failed", result) { message ->
                    showToast(message)
                }
            }
        }
    }

    override fun launchSignOutFlow() {
        if (!authenticator.isUserSignedIn()) {
            return error("no user is signed in, cannot sign out") {
                showToast("Can't sign out, you're not signed in")
            }
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                authenticator.signOut()
            }

            if (result.isSuccess) {
                authState.emit(AuthState(null))
                showToast("Signed out")
                Log.i(TAG, "signed out")
            } else {
                error("Sign out failed", result) { message ->
                    showToast(message)
                }
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
                            showToast("Sync complete")
                        }
                        .show()

                    try {
                        withContext(Dispatchers.IO) {
                            syncDataService.sync(SyncMode.OVERWRITE).collect { resultOf ->
                                if (resultOf.isFailure) {
                                    withContext(Dispatchers.Main) {
                                        showToast("Sync failed for ${resultOf.subject}")
                                    }
                                } else {
                                    Log.i(TAG, "successfully synced ${resultOf.subject}")
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        error("sync data service failed: ${t.message}", t) {
                            showToast("Sync failed...")
                        }
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

    @Composable
    private fun SnackbarHost(content: @Composable () -> Unit) {

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        modifier = Modifier.padding(22.dp),
                        action = {
                            if (snackbar.value!!.action != null) {
                                Button(
                                    onClick = snackbar.value!!.action!!.onClick
                                ) {
                                    Text(snackbar.value!!.action!!.text)
                                }
                            }
                        }
                    ) {
                        if (snackbar.value != null) {
                            Text(snackbar.value!!.message)
                        }
                    }
                }
            }
        ) { padding ->
            Log.d(TAG, "ignoring padding from scaffold $padding")
            content()
        }

        if (snackbar.value != null) {
            LaunchedEffect(snackbar.value) {
                Log.v(TAG, "launched effect on ${snackbar.value}")
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "",
                        duration = snackbar.value!!.duration
                    )
                }
            }
        }
    }
}

@NotThreadSafe
private class AppControllerSettings private constructor(dataStoreManager: DataStoreManager) {

    companion object {

        private val USERS_KEY = stringSetPreferencesKey("users")

        private var instance: AppControllerSettings? = null

        /**
         * Return the singleton instance of [AppControllerSettings]
         * */
        suspend fun getInstance(dataStoreManager: DataStoreManager): AppControllerSettings {
            return instance ?: AppControllerSettings(dataStoreManager).apply {
                try {
                    val preferences = withContext(Dispatchers.IO) {
                        preferencesStore.data.first()
                    }
                    val userIds: Set<String>? = preferences[USERS_KEY]
                    if (userIds != null) {
                        users.addAll(userIds)
                    }
                } catch (ioe: IOException) {
                    error("failed to read app controller settings from preferences store", ioe)
                }

                instance = this
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