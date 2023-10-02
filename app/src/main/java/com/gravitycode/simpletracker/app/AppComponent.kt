package com.gravitycode.simpletracker.app

import android.app.Application
import com.gravitycode.simpletracker.workout_list.WorkoutListActivity
import dagger.BindsInstance
import dagger.Component

/**
 * TODO: Can't even remember how these work. Need to read
 * https://developer.android.com/training/dependency-injection/dagger-android#dagger-scopes again.
 * Does everything have to be scoped? Seemingly any @Provides method or @Inject constructor,
 * I imagine @Binds too?
 * */
@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: WorkoutListActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): AppComponent
    }
}