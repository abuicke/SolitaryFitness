package com.gravitycode.solitaryfitnessapp.app

import android.app.Application
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

    fun inject(app: SolitaryFitnessApp)
}