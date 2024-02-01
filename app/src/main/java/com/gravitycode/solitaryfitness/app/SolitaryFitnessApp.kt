package com.gravitycode.solitaryfitness.app

import android.app.Application
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.util.android.debug
import com.gravitycode.solitaryfitness.util.android.disableLogcatThrottling
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.Executor
import javax.inject.Inject

class SolitaryFitnessApp : Application() {

    @Inject lateinit var applicationExecutor: Executor

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
        appStateFlow: SharedFlow<AppState>,
        flowLauncher: FlowLauncher
    ): ActivityComponent {
        return applicationComponent.activityComponentBuilder()
            .componentActivity(activity)
            .appStateFlow(appStateFlow)
            .flowLauncher(flowLauncher)
            .build()
    }
}