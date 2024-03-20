package dev.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import dev.gravitycode.caimito.kotlin.ui.Messenger
import dev.gravitycode.solitaryfitness.auth.AuthModule
import dev.gravitycode.solitaryfitness.auth.AuthenticationStatus
import dev.gravitycode.solitaryfitness.logworkout.LogWorkoutComponent
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [AuthModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        @BindsInstance fun messenger(messenger: Messenger): Builder

        @BindsInstance fun authenticationObservable(observable: AuthenticationStatus): Builder

        @BindsInstance fun flowLauncher(flowLauncher: FlowLauncher): Builder

        fun build(): ActivityComponent
    }

    fun logWorkoutComponentBuilder(): LogWorkoutComponent.Builder

    // fun inject(activity: MainActivity)
}