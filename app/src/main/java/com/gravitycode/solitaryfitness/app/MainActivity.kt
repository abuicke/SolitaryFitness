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
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.log_workout.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.util.data.createPreferencesStoreFromFile
import com.gravitycode.solitaryfitness.util.data.stringSetPreferencesKey
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * "When you repeat yourself 3 times, then refactor..."
 *
 *
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 * TODO: Need to write rigorous tests for the [LogWorkoutViewModel]
 *
 * TODO: `com.firebase.ui.auth.FirebaseUiException: Code: 7, message: 7` happens randomly. When an
 *  authentication exception occurs, need to handle it gracefully and show a toast asking the user to sign
 *  in again.
 * TODO: When a user signs in, ask "Would you like to save your offline work to your account?" and if they
 *  say "Yes", upload all Preferences Store logs to Firestore while an async task dialog (or its equivalent)
 *  shows "Syncing...". Will need to retain a Set<String> of all dates a record is stored for so I can easily
 *  iterate over them and upload them to Firestore.
 *
 * TODO: It doesn't seem like dispatchers should be specified by the calling code as they're not aware of
 *  the implementation details of what they're calling. It seems like this should be done with [withContext]
 *  in the function being called.
 * TODO: For `launchTransferDataFlow()` to be called there should be some work to transfer. The user
 *  shouldn't have immediately signed in first time they launched the app and then receive this message.
 *  The logic I will need later in the process to iterate over all available records will also be required now.
 * TODO: Figure out multiple data stores crash. Log where data store is created and see how many times it
 *  happens.
 * TODO: Need to specify launch(Dispatchers.IO) for all coroutine operations (lifecycleScope and viewModelScope)
 *  that involve accessing disk or network (so any time I access a repository).
 * TODO: Allow customizing rep count on a per workout basis via long pressing the rep count
 * TODO: Need to have a long snackbar with an undo option when reset is clicked
 * TODO: Need to check if phone is online to sign in
 * TODO: Test Firebase works offline. Throws an offline exception when mobile data is enabled.
 * TODO: When the app is profiled the memory increases and then stays there, particularly when the user logs
 *  in and the Firestore repo is assumedly initialized. How can I make it so that only one repo is ever
 *  retained in memory? Use a WeakReference? Create WeakLazyWorkoutLogRepository?
 * TODO: What happens when read, write or update is called multiple times quickly (assumedly while one of
 *  the earlier calls is still executing)?
 * TODO: When any part of the LogWorkoutState gets updated, e.g. the date, every other composable that gets
 *  a value from the state seems to recompose. Is there a more efficient way of doing this? Is this why
 *  the DateSelected event gets triggered 3 times in a row for the screen to be displayed or is it something else?
 * TODO: Is there a race condition between the ViewModel completing its setup and the composable being put
 *  on the screen? If reps are added before the repository has initialized assumedly the app will crash?
 * TODO: Need to write checks that a date in the future is never submitted. I suppose that's handled by the
 *  ViewModel, should it also be checked for in the repository? Yes, I think so. No class should rely on the
 *  error handling of any other. It should do its own checks. I'm just wondering if data sanitization is
 *  commonly handled in the repository? I suppose that's the whole point of abstracting out an interface
 *  to interact with your data source.
 * TODO: What happens when [Authenticator.signIn] or [Authenticator.signOut] is called multiple times?
 * TODO: Use snackbar instead of toast for sign in and sign out, also notify for all 4:
 *      1) Successful sign in: "Signed in as {email}"
 *      2) Successful sign out: "Signed out"
 *      3) Failure sign in: "Failed to sign in"
 *      4) Failure sign out: "Failed to sign out"
 *
 * TODO: Overflow:
 *          Sign In/Sign Out
 *          Reset Reps
 *          Edit Reps - Adjust rep counts in each cell.
 *          Settings:
 *              1) Set custom values for rep buttons (All: 1, 5, 10) or on a per workout basis
 *              2) Boolean option: keep reps grid open until X is selected
 *              3) Clear history: offline, online or both
 *              4) Transfer offline progress to account (in case the user wants to do it later)
 *
 *
 *
 *
 *
 * TODO: Is there space to make use of an object pool anywhere? Such as when I'm copying the WorkoutLogs
 *  over and over again in the [LogWorkoutViewModel] or just generally when I'm returning the objects from
 *  the repos etc.? Anywhere `copy()` is used would be a good candidate
 *  (Android Pools Helper Class)[https://developer.android.com/reference/androidx/core/util/Pools]
 *
 *  TODO: I haven't been using UseCases. Where do they belong? In accessing the DataStore? Remember
 *      you can override the invoke operator `()` so they can be called like `XxUseCase(args...)`
 *
 * TODO: Run ProGuard on app for build
 *
 * TODO: There's a multiple DataStore instances exception on first install.
 *  When you run again after that it launches without issue.
 *  This doesn't seem to a problem in the release build or when it's downloaded from the PlayStore.
 *
 * TODO: Add Google Fit integration
 *
 * TODO: Implement number change animation. Like if the user clicks +10 you see the reps quickly
 *  count up from the current reps to +10.
 * */
class MainActivity : ComponentActivity(), AppController {

    private companion object {

        const val TAG = "MainActivity"
    }

    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var toaster: Toaster
    @Inject lateinit var logWorkoutViewModel: LogWorkoutViewModel

    override val applicationScope = MainScope()
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
                    TrackRepsScreen(logWorkoutViewModel)
                }
            }
        }
    }

    override fun requestSignIn() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = authenticator.signIn()
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                appState.emit(AppState(user))
                if (!appControllerSettings.hasUserPreviouslySignedIn(user)) {
                    appControllerSettings.addUserToSignInHistory(user)
                    launchTransferDataFlow()
                }
                withContext(Dispatchers.Main) {
                    toaster("Signed in: ${user.email}")
                }
                Log.d(TAG, "signed in as user: $user")
            } else {
                toaster("Failed to sign in")
                debugError("Sign in failed", result)
            }
        }
    }

    override fun requestSignOut() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = authenticator.signOut()
            if (result.isSuccess) {
                appState.emit(AppState(null))
                withContext(Dispatchers.Main) {
                    toaster("Signed out")
                }
                Log.v(TAG, "signed out")
            } else {
                toaster("Failed to sign out")
                debugError("Sign out failed", result)
            }
        }
    }

    override fun launchTransferDataFlow() {
        AlertDialog.Builder(this)
            .setTitle(R.string.transfer_dialog_title)
            .setMessage(R.string.transfer_dialog_message)
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