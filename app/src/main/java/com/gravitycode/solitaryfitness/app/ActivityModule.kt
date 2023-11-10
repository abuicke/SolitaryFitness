package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gravitycode.solitaryfitness.auth.Authenticator
import com.gravitycode.solitaryfitness.auth.FirebaseAuthenticator
import com.gravitycode.solitaryfitness.track_reps.data.FirestoreWorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.data.WorkoutHistoryRepo
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {

//    @Provides
//    @ActivityScope
//    fun providesWorkoutHistoryPreferencesStore(context: Context): DataStore<Preferences> {
//        return PreferenceDataStoreFactory.create {
//            context.preferencesDataStoreFile("workout_history")
//        }
//    }
//
//    @Provides
//    @ActivityScope
//    fun provideWorkoutHistoryRepo(preferencesStore: DataStore<Preferences>): WorkoutHistoryRepo =
//        PreferencesWorkoutHistoryRepo(preferencesStore)

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
    fun provideWorkoutHistoryRepo(
        firestore: FirebaseFirestore,
        authenticator: Authenticator
    ): WorkoutHistoryRepo =
        FirestoreWorkoutHistoryRepo(firestore, authenticator)

    @Provides
    @ActivityScope
    fun provideTrackRepsViewModel(toaster: Toaster, repo: WorkoutHistoryRepo) =
        TrackRepsViewModel(toaster, repo)
}