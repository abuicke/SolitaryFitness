package com.gravitycode.solitaryfitness.app

import android.app.Application
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.auth.AuthenticationObservable
import com.gravitycode.solitaryfitness.util.android.Messenger
import com.gravitycode.solitaryfitness.util.android.debug
import com.gravitycode.solitaryfitness.util.android.disableLogcatThrottling

class SolitaryFitnessApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .build()
        applicationComponent.inject(this)

        debug {
            disableLogcatThrottling()
        }
    }

    fun activityComponent(
        activity: ComponentActivity,
        messenger: Messenger,
        authenticationObservable: AuthenticationObservable,
        flowLauncher: FlowLauncher
    ): ActivityComponent {
        return applicationComponent.activityComponentBuilder()
            .componentActivity(activity)
            .messenger(messenger)
            .authenticationObservable(authenticationObservable)
            .flowLauncher(flowLauncher)
            .build()
    }

    fun activityComponent(activity: ComponentActivity) = activityComponent(
        activity,
        activity as Messenger,
        activity as AuthenticationObservable,
        activity as FlowLauncher
    )
}