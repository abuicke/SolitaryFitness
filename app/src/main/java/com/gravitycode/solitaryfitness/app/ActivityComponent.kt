package com.gravitycode.solitaryfitness.app

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.auth.AuthModule
import com.gravitycode.solitaryfitness.logworkout.LogWorkoutComponent
import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.flow.Flow

@ActivityScope
@Subcomponent(modules = [AuthModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(app: ComponentActivity): Builder

        @BindsInstance fun appStateFlow(appStateFlow: Flow<AppState>): Builder

        @BindsInstance fun flowLauncher(flowLauncher: FlowLauncher): Builder

        fun build(): ActivityComponent
    }

    fun logWorkoutComponentBuilder(): LogWorkoutComponent.Builder

    // fun inject(activity: MainActivity)
}