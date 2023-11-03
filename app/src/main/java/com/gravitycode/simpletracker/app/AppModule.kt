package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import com.gravitycode.solitaryfitness.track_reps.TrackRepsComponent
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module(subcomponents = [TrackRepsComponent::class])
object AppModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster(context)
}