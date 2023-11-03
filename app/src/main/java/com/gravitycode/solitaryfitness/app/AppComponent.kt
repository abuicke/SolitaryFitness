package com.gravitycode.solitaryfitness.app

import android.app.Application
import com.gravitycode.solitaryfitness.track_reps.TrackRepsComponent
import dagger.BindsInstance
import dagger.Component

/**
 * Test Dagger isn't producing different instances when it shouldn't and different instances
 * when it should like this (alternatively inject the dependencies into the relevant activity
 * and test in the same way with `assert`):
 *
 *      val applicationGraph: ApplicationGraph = DaggerApplicationGraph.create()
 *
 *      val userRepository: UserRepository = applicationGraph.repository()
 *      val userRepository2: UserRepository = applicationGraph.repository()
 *
 *      assert(userRepository != userRepository2)
 *
 * @see [https://developer.android.com/training/dependency-injection/dagger-basics#dagger-components]
 * */
@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun trackRepsComponent(): TrackRepsComponent.Factory
}