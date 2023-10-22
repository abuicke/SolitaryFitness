package com.gravitycode.simpletracker.app

import android.app.Application
import android.content.Context
import com.gravitycode.simpletracker.track_reps.TrackRepsComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [TrackRepsComponent::class])
object AppModule {

    @Provides fun providesApplicationContext(app: Application): Context = app.applicationContext
}