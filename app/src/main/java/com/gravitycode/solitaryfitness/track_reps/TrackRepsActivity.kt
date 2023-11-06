package com.gravitycode.solitaryfitness.track_reps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gravitycode.solitaryfitness.R
import com.gravitycode.solitaryfitness.app.SolitaryFitnessApp
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.debugError
import javax.inject.Inject

/**
 * "When you repeat yourself 3 times, then refactor..."
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 *
 * TODO: Sync data to Firebase (or somewhere) to make sure the record is never lost.
 * TODO: Put profile pic in the toolbar when user signs in.
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

    @Inject lateinit var firestore: FirebaseFirestore

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
//        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
//        AuthUI.IdpConfig.FacebookBuilder().build(),
//        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    // Create and launch sign-in intent
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        if (result.idpResponse != null) {
            val idpResponse: IdpResponse = result.idpResponse!!
            if(idpResponse.error != null) {
                debugError("Sign in failed", idpResponse.error)
            }
        }

        val response = result.idpResponse
        Log.i("mo", "response = $response")

        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.i("mo", "current user = $user")

            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    Log.i("mo", "signed out")
                }
        } else {
            Log.e("mo", "sign in failed, result code = ${result.resultCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SolitaryFitnessApp).appComponent
        trackRepsComponent = appComponent.trackRepsComponent().create()
        trackRepsComponent.inject(this)

        signInLauncher.launch(signInIntent)

//        setContent {
//            SolitaryFitnessTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    TrackRepsScreen(
//                        trackRepsState = trackRepsViewModel.state.value,
//                        onEvent = trackRepsViewModel::onEvent
//                    )
//                }
//            }
//        }
    }
}