package com.gravitycode.solitaryfitness.app

import android.app.Application
import com.gravitycode.solitaryfitness.logworkout.LogWorkoutComponent
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }

    fun activityComponentBuilder(): ActivityComponent.Builder

    // fun inject(activity: MainActivity)
}