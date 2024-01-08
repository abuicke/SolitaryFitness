package com.gravitycode.solitaryfitness.auth

import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.di.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class Private

@Module
object AuthModule {

    @Private
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Private
    @Provides
    fun providesFirebaseAuthUi() = AuthUI.getInstance()

    @Provides
    @ActivityScope
    fun providesAuthenticator(
        activity: ComponentActivity,
        @Private auth: FirebaseAuth,
        @Private ui: AuthUI
    ): Authenticator {
        return FirebaseAuthenticator(activity, auth, ui)
    }
}