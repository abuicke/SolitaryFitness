package com.gravitycode.solitaryfitness.di

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.app.ActivityModule
import com.gravitycode.solitaryfitness.app.MainActivity
import com.gravitycode.solitaryfitness.auth.AuthModule
import com.gravitycode.solitaryfitness.firebase.FirebaseModule
import com.gravitycode.solitaryfitness.log_workout.LogWorkoutModule
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ActivityModule::class,
        AuthModule::class,
        FirebaseModule::class,
        LogWorkoutModule::class
    ]
)
interface ActivityComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        fun build(): ActivityComponent
    }

    fun inject(activity: MainActivity)
}