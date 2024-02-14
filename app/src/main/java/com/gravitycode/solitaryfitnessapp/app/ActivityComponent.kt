package com.gravitycode.solitaryfitnessapp.app

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitnessapp.auth.AuthModule
import com.gravitycode.solitaryfitnessapp.auth.AuthenticationObservable
import com.gravitycode.solitaryfitnessapp.logworkout.LogWorkoutComponent
import com.gravitycode.solitaryfitnessapp.util.android.Messenger
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [AuthModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        @BindsInstance fun messenger(messenger: Messenger): Builder

        @BindsInstance fun authenticationObservable(observable: AuthenticationObservable): Builder

        @BindsInstance fun flowLauncher(flowLauncher: FlowLauncher): Builder

        fun build(): ActivityComponent
    }

    fun logWorkoutComponentBuilder(): LogWorkoutComponent.Builder

    // fun inject(activity: MainActivity)
}