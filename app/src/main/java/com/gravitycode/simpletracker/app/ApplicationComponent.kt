package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workout_list.WorkoutListActivity
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    /**
     * TODO: Can this function be called anything? Are all functions exposed by components automatically injection sites?
     * TODO: Should this injection be happening here? Shouldn't it be happening in
     * [com.gravitycode.simpletracker.workout_list.WorkoutListComponent]? Can I share [ApplicationComponent]
     * as a parent to all the feature components, e.g. [com.gravitycode.simpletracker.workout_list.WorkoutListComponent]?
     * */
    fun inject(activity: WorkoutListActivity)
}