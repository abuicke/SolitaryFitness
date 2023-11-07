package com.gravitycode.solitaryfitness.auth

import android.util.Log
import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.util.data.GetActivityResult
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.Toaster

class FirebaseAuthenticator(
    private val activity: ComponentActivity,
    private val toaster: Toaster
) : Authenticator {

    /**
     * Authentication providers
     * */
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        // AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        // AuthUI.IdpConfig.FacebookBuilder().build(),
        // AuthUI.IdpConfig.TwitterBuilder().build()
    )

    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    private val getActivityResult = GetActivityResult(
        activity,
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.idpResponse != null) {
            val idpResponse: IdpResponse = result.idpResponse!!
            if (idpResponse.error != null) {
                toaster("Failed to login")
                debugError("Sign in failed", idpResponse.error)
            }
        }

        val response = result.idpResponse
        Log.i("mo", "response = $response")

        if (result.resultCode == ComponentActivity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.i("mo", "current user = $user")

            signOut()
        } else {
            Log.e("mo", "sign in failed, result code = ${result.resultCode}")
        }
    }

    override fun signIn() {
        getActivityResult(signInIntent)
    }

    override fun signOut() {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                Log.i("mo", "signed out")
            }
    }
}