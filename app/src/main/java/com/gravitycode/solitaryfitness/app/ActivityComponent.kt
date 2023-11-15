package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.auth.AuthModule
import com.gravitycode.solitaryfitness.di.ActivityScope
import com.gravitycode.solitaryfitness.log_workout.LogWorkoutModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [AuthModule::class, LogWorkoutModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        fun build(): ActivityComponent
    }

    fun inject(activity: MainActivity)
}