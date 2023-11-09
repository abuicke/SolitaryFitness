package com.gravitycode.solitaryfitness.auth

import android.util.Log
import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.util.data.GetActivityResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @see [com.firebase.ui.auth.ErrorCodes] for error code meanings
 * */
class FirebaseAuthenticator(
    private val activity: ComponentActivity
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

    private val contract = FirebaseAuthUIActivityResultContract()
    private val getFirebaseSignInResult = GetActivityResult(activity, contract)

    private var user: User? = null

    override suspend fun signIn(): Result<User> {
        val result = getFirebaseSignInResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        )

        val response = result.idpResponse

        return if (result.resultCode == ComponentActivity.RESULT_OK) {
            Log.d(TAG, "sign in successful\n$response")
            val firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val user = User(firebaseUser)
            this.user = user
            Result.success(user)
        } else {
            val error: Throwable = if (response != null && response.error != null) {
                response.error!!
            } else {
                AuthenticationException(
                    "unspecified firebase ui exception, result code: ${result.resultCode}"
                )
            }

            Result.failure(error)
        }
    }

    override suspend fun signOut(): Result<Unit> = suspendCoroutine { continuation ->
        AuthUI.getInstance()
            .signOut(activity)
            .addOnFailureListener {
                val errMsg = "failed to sign out user: $user"
                Log.d(TAG, errMsg)
                val exception = AuthenticationException(errMsg)
                val result = Result.failure<Unit>(exception)
                continuation.resume(result)
            }
            .addOnSuccessListener {
                Log.d(TAG, "signed out user $user")
                user = null
                val result = Result.success(Unit)
                continuation.resume(result)
            }
    }

    override fun isUserSignedIn() = user != null

    override fun getSignedInUser() = user
}