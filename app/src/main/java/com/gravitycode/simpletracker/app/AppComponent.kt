package com.gravitycode.simpletracker.app

import android.app.Application
import com.gravitycode.simpletracker.workout_list.WorkoutListComponent
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun workoutListComponent(): WorkoutListComponent.Factory
}