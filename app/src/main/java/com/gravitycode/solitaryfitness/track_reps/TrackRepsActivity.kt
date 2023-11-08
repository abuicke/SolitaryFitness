package com.gravitycode.solitaryfitness.track_reps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.app.SolitaryFitnessApp
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.ui.Toaster
import javax.inject.Inject

/**
 *
 *
 * TODO: After completing sign in from TopBar, add Menu https://developer.android.com/develop/ui/views/components/menus
 *  the Sign In option should be replaced with Sign Out if the user is signed in. Will need a way of
 *  telling [TrackRepsScreen] whether the user is signed in or not. Is there a better way of doing this
 *  than just putting it in the ViewModel?
 *
 *
 *
 *
 *
 *
 *
 * "When you repeat yourself 3 times, then refactor..."
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 *
 * TODO: Sync data to Firebase (or somewhere) to make sure the record is never lost.
 * TODO: Put profile pic in the toolbar when user signs in.
 * TODO: The name is available signing in the long way but not with SmartLock. There might be a way to
 *  disable SmartLock from ever showing up in [AuthUI.AuthIntentBuilder] if I decide I want the name.
 * TODO: How to make the full FirebaseAuthUI less ugly?
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
class TrackRepsActivity : ComponentActivity() {

    private lateinit var trackRepsComponent: TrackRepsComponent
    @Inject lateinit var trackRepsViewModel: TrackRepsViewModel

    @Inject lateinit var toaster: Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SolitaryFitnessApp).appComponent
        trackRepsComponent = appComponent.trackRepsComponent().componentActivity(this).build()
        trackRepsComponent.inject(this)

        val authenticator: Authenticator = FirebaseAuthenticator(this, toaster)

        setContent {
            SolitaryFitnessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackRepsScreen(
                        isUserSignedIn = authenticator.isUserSignedIn(),
                        trackRepsState = trackRepsViewModel.state.value,
                        onEvent = trackRepsViewModel::onEvent
                    )
                }
            }
        }
    }
}