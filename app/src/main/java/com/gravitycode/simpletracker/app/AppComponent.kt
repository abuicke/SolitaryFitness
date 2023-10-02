package com.gravitycode.simpletracker.app

import android.app.Application
import com.gravitycode.simpletracker.workout_list.WorkoutListActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: WorkoutListActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): AppComponent
    }
}