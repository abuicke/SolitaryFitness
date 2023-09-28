package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workout_list.WorkoutListActivity
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(activity: WorkoutListActivity)
}