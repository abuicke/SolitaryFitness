package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        fun build(): ActivityComponent
    }

    fun inject(activity: MainActivity)
}