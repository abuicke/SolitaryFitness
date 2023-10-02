package com.gravitycode.simpletracker.workout_list

import com.gravitycode.simpletracker.app.ActivityScope
import dagger.Subcomponent

//@ActivityScope
@Subcomponent(modules = [WorkoutListModule::class])
interface WorkoutListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): WorkoutListComponent
    }

    fun inject(activity: WorkoutListActivity)
}