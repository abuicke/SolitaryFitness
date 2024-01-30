package com.gravitycode.solitaryfitness.auth

import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.app.ActivityScope
import com.gravitycode.solitaryfitness.logworkout.LogWorkoutComponent
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
private annotation class InternalDependency

/**
 * TODO: If a more general module (like an ActivityModule) becomes available, move this there.
 * */
@Module(subcomponents = [LogWorkoutComponent::class])
object AuthModule {

    @Provides
    @InternalDependency
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @InternalDependency
    fun providesFirebaseAuthUi() = AuthUI.getInstance()

    @Provides
    @ActivityScope
    fun providesAuthenticator(
        activity: ComponentActivity,
        @InternalDependency auth: FirebaseAuth,
        @InternalDependency ui: AuthUI
    ): Authenticator {
        return FirebaseAuthenticator(activity, auth, ui)
    }
}