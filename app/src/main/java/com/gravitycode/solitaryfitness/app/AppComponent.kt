package com.gravitycode.solitaryfitness.app

import android.app.Application
import com.gravitycode.solitaryfitness.firebase.FirebaseModule
import com.gravitycode.solitaryfitness.app.ui.UiModule
import com.gravitycode.solitaryfitness.di.ApplicationScope
import dagger.BindsInstance
import dagger.Component

/**
 * Modules represent groups of related functionality and bindings. In contrast, a Subcomponent usually
 * represents a different scope and lifecycle.
 *
 * (dagger-2-modules-vs-subcomponents)[https://stackoverflow.com/a/51659903/4596649]
 *
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
 * (dagger-basics#dagger-components)[https://developer.android.com/training/dependency-injection/]
 * */
@ApplicationScope
@Component(modules = [AppModule::class, UiModule::class, FirebaseModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun activityComponent(): ActivityComponent.Builder
}