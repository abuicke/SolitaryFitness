package com.gravitycode.solitaryfitness.auth

import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
object AuthModule {

    @Provides
    @ActivityScope
    fun providesAuthenticator(activity: ComponentActivity, auth: FirebaseAuth, ui: AuthUI): Authenticator {
        return FirebaseAuthenticator(activity, auth, ui)
    }
}