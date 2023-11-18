package com.gravitycode.solitaryfitness.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.util.ui.Toaster
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.di.DaggerActivityComponent
import com.gravitycode.solitaryfitness.log_workout.data.WorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.log_workout.presentation.LogWorkoutViewModel
import com.gravitycode.solitaryfitness.log_workout.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.util.debugError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * "When you repeat yourself 3 times, then refactor..."
 *
 *
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 *
 * TODO: Test Firebase works offline. Throws an offline exception when mobile data is enabled.
 * TODO: Verify that Dagger isn't creating either repository until `get()` is called.
 * TODO: If the user is signed out and then signs in, or is signed in and then signs out, are both repositories
 *  held in memory? How to make it so only the repo which is currently being used is held in memory? Or is
 *  that even worth doing?
 * TODO: Implement ViewModel Factory with a Screen enum for parameter?
 * TODO: Replace @see with proper markdown everywhere it's referencing a URL
 * TODO: Test that activity lifecycle check in [com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator]
 *  works correctly. Construct the object in each stage of the [MainActivity]. I don't think I can write an
 *  actual test.
 * TODO: Refactor: Replace "repo" with "repository" and do the same for all other shorthands.
 * TODO: Need to write checks that a date in the future is never submitted. I suppose that's handled by the
 *  ViewModel, should it also be checked for in the repository?
 * TODO: Need to use `PreferencesWorkoutHistoryRepo` when the user is logged out. Just maintain two independent records?
 *  It doesn't make sense to expect records that are recorded when you're logged in to be available when you
 *  log out, but I should upload all records stored in preferences to Firestore when the user logs in.
 * TODO: Put profile pic in the toolbar when user signs in. Use Glide? Is there a Kotlin-first solution?
 * TODO: FirebaseUI crashes when there's no internet connection. Test without internet connection and resolve.
 * TODO: What happens when [Authenticator.signIn] or [Authenticator.signOut] is called multiple times?
 * TODO: Use snackbar instead of toast for sign in and sign out, also notify for all 4:
 *      1) Successful sign in: "Signed in as {email}"
 *      2) Successful sign out: "Signed out"
 *      3) Failure sign in: "Failed to sign in"
 *      4) Failure sign out: "Failed to sign out"
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
 *
 *
 * TODO: Is there space to make use of an object pool anywhere? Such as when I'm copying the WorkoutLogs
 *  over and over again in the [LogWorkoutViewModel] or just generally when I'm returning the objects from
 *  the repos etc.? Anywhere `copy()` is used would be a good candidate
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

    override val appState = MutableStateFlow(AppState(null))

    /**
     *
     *
     * TODO: Replace AppController.StateFlow with SharedFlow so that [LogWorkoutViewModel] can subscribe to
     *  it and automatically retrieve the repository it needs from the factory.
     *
     *
     *
     *
     * */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerActivityComponent.builder()
            .componentActivity(this)
            .build()
            .inject(this)

        val currentUser = authenticator.getSignedInUser()
        appState.value = AppState(currentUser)

        setContent {
            SolitaryFitnessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackRepsScreen(
                        viewModel = logWorkoutViewModel,
                        appController = this
                    )
                }
            }
        }
    }

    override fun requestSignIn() {
        lifecycleScope.launch {
            val result = authenticator.signIn()
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                appState.value = AppState(user)
                toaster("Signed in: ${user.email}")
                Log.d(TAG, "signed in as user: $user")
            } else {
                toaster("Failed to sign in")
                debugError("Sign in failed", result)
            }
        }
    }

    override fun requestSignOut() {
        lifecycleScope.launch {
            val result = authenticator.signOut()
            if (result.isSuccess) {
                appState.value = AppState(null)
                toaster("Signed out")
                Log.v(TAG, "signed out")
            } else {
                toaster("Failed to sign out")
                debugError("Sign out failed", result)
            }
        }
    }
}