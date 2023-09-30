package com.gravitycode.simpletracker.workout_list

import dagger.Subcomponent

@Subcomponent
interface WorkoutListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): WorkoutListComponent
    }

    fun inject(activity: WorkoutListActivity)
}