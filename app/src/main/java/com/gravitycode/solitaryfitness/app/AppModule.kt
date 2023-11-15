package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ActivityComponent::class])
object AppModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext
}