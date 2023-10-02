package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workout_list.WorkoutListComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, SubcomponentsModule::class])
interface ApplicationComponent {

    fun workoutListComponent(): WorkoutListComponent.Factory
}