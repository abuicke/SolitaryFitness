package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.gravitycode.solitaryfitness.track_reps.TrackRepsComponent
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module(subcomponents = [TrackRepsComponent::class])
object AppModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    fun providesFirebaseFirestore() = Firebase.firestore

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster(context)
}