package com.gravitycode.solitaryfitness.logworkout

import dagger.Subcomponent

@LogWorkoutScope
@Subcomponent(modules = [LogWorkoutModule::class])
interface LogWorkoutComponent {

    @Subcomponent.Builder
    interface Builder {

        fun build(): LogWorkoutComponent
    }

    fun inject(activity: LogWorkoutActivity)
}