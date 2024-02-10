package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.auth.AuthModule
import com.gravitycode.solitaryfitness.auth.AuthenticationObservable
import com.gravitycode.solitaryfitness.logworkout.LogWorkoutComponent
import com.gravitycode.solitaryfitness.util.android.Messenger
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