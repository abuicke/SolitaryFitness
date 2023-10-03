package com.gravitycode.simpletracker.app

import android.app.Application
import android.content.Context
import com.gravitycode.simpletracker.workout_list.WorkoutListComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [WorkoutListComponent::class])
class AppModule {

    @Provides fun providesApplicationContext(app: Application): Context = app.applicationContext
}