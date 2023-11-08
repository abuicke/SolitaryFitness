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

    companion object {
        private const val TAG = "FirebaseAuthenticator"
    }

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
        val response = result.idpResponse
        Log.i(TAG, "identity provider response = $response")

        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                user = User(firebaseUser)
            }
            Log.i(TAG, "current user = $user")
        } else {
            Log.e(TAG, "sign in failed, result code = ${result.resultCode}")
            if (result.idpResponse != null) {
                val idpResponse: IdpResponse = result.idpResponse!!
                if (idpResponse.error != null) {
                    toaster("Failed to login")
                    debugError("Sign in failed", idpResponse.error)
                }
            }
        }
    }

    private var user: User? = null

    override fun signIn() {
        getActivityResult(signInIntent)
    }

    /**
     * TODO: Need a way of knowing if sign out was successful. Should probably return
     *  result in which case it will probably have to be a suspend function. Can I create
     *  a Flow<Result> from the multiple possible listeners, i.e. [com.google.android.gms.tasks.OnCanceledListener],
     *  [com.google.android.gms.tasks.OnCompleteListener], [com.google.android.gms.tasks.OnFailureListener],
     *  [com.google.android.gms.tasks.OnSuccessListener]. Are all the listeners necessary? Does the
     *  onComplete listener combine all of them?
     * */
    override fun signOut() {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                Log.i(TAG, "signed out user $user")
                user = null
            }
    }

    override fun isUserSignedIn() = user != null

    override fun getSignedInUser() = user
}