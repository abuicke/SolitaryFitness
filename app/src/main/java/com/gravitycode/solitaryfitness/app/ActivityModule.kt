package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.app.ActivityScope
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
    fun providesAuthenticator(activity: ComponentActivity): Authenticator =
        FirebaseAuthenticator(activity)

    /**
     * TODO: Should be replaced by a factory?
     * */
    @Provides
    @ActivityScope
    fun provideTrackRepsViewModel(toaster: Toaster, repo: WorkoutHistoryRepo): TrackRepsViewModel =
        TrackRepsViewModel(toaster, repo)
}