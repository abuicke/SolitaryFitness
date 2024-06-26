package dev.gravitycode.solitaryfitness.app

import android.app.Application
import androidx.activity.ComponentActivity
import dev.gravitycode.caimito.kotlin.android.debug
import dev.gravitycode.caimito.kotlin.android.disableLogcatThrottling
import dev.gravitycode.caimito.kotlin.core.AppConfiguration
import dev.gravitycode.caimito.kotlin.ui.Messenger
import dev.gravitycode.solitaryfitness.BuildConfig
import dev.gravitycode.solitaryfitness.auth.AuthenticationStatus

class SolitaryFitnessApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .build()
        applicationComponent.inject(this)

        AppConfiguration.setup(
            debug = BuildConfig.DEBUG,
            crashOnError = BuildConfig.CRASH_ON_ERROR
        )

        debug {
            disableLogcatThrottling()
        }
    }

    fun activityComponent(
        activity: ComponentActivity,
        messenger: Messenger,
        authStatus: AuthenticationStatus,
        flowLauncher: FlowLauncher
    ): ActivityComponent {
        return applicationComponent.activityComponentBuilder()
            .componentActivity(activity)
            .messenger(messenger)
            .authenticationStatus(authStatus)
            .flowLauncher(flowLauncher)
            .build()
    }

    fun activityComponent(activity: ComponentActivity) = activityComponent(
        activity,
        activity as Messenger,
        activity as AuthenticationStatus,
        activity as FlowLauncher
    )
}