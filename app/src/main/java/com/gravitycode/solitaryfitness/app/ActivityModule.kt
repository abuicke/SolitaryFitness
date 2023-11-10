package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator
import com.gravitycode.solitaryfitness.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {

    @Provides
    @ActivityScope
    fun providesAuthenticator(
        activity: ComponentActivity,
        auth: FirebaseAuth,
        authUI: AuthUI
    ): Authenticator =
        FirebaseAuthenticator(activity, auth, authUI)

    @Provides
    @ActivityScope
    fun provideTrackRepsViewModel(toaster: Toaster, repo: WorkoutHistoryRepo) =
        TrackRepsViewModel(toaster, repo)
}