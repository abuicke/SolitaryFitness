package com.gravitycode.solitaryfitness.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Toaster
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * "When you repeat yourself 3 times, then refactor..."
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 *
 * TODO: Sync data to Firebase (or somewhere) to make sure the record is never lost.
 * TODO: Put profile pic in the toolbar when user signs in.
 * TODO: The name is available signing in the long way but not with SmartLock. There might be a way to
 *  disable SmartLock from ever showing up in [AuthUI.AuthIntentBuilder] if I decide I want the name.
 * TODO: How to make the full FirebaseAuthUI less ugly?
 * TODO: FirebaseUI crashes when there's no internet connection. Test without internet connection and resolve.
 * TODO: Use snackbar instead of toast for sign in and sign out, also notify for all 4:
 *  1) Successful sign in
 *  2) Successful sign out
 *  3) Failure sign in
 *  4) Failure sign out
 *
 * TODO: Overflow:
 *          Sign In
 *          Reset Reps
 *          Edit Reps - Adjust rep counts in each cell.
 *          Settings:
 *              1) Set custom values for rep buttons
 *              2) Boolean option: keep reps grid open until X is selected
 *
 *
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
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val appState = mutableStateOf(AppState())

    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var toaster: Toaster

    private lateinit var activityComponent: ActivityComponent
    @Inject lateinit var trackRepsViewModel: TrackRepsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SolitaryFitnessApp).appComponent
        activityComponent = appComponent.activityComponent().componentActivity(this).build()
        activityComponent.inject(this)

        setContent {
            SolitaryFitnessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackRepsScreen(
                        appState = appState.value,
                        trackRepsState = trackRepsViewModel.state.value,
                        onAppEvent = this::handleAppEvent,
                        onEvent = trackRepsViewModel::onEvent
                    )
                }
            }
        }
    }

    private fun handleAppEvent(appEvent: AppEvent) {
        when (appEvent) {
            AppEvent.SignIn -> signIn()
            AppEvent.SignOut -> signOut()
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            val result = authenticator.signIn()
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                appState.value = AppState(user)
                Log.d(TAG, "signed in as user: $user")
            } else {
                val exception = result.exceptionOrNull()
                toaster("Failed to login")
                debugError("Sign in failed", exception)
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            val result = authenticator.signOut()
            if (result.isSuccess) {
                appState.value = AppState(null)
            } else {
                val exception = result.exceptionOrNull()
                toaster("Failed to log out")
                debugError("Sign out failed", exception)
            }
        }
    }
}