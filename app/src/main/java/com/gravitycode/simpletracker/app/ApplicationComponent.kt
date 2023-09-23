package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workouts_list.WorkoutListActivity
import dagger.Component

@Component
interface ApplicationComponent {

    fun inject(activity: WorkoutListActivity)
}