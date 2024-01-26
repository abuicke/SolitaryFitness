package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.os.Build
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.util.android.debug
import com.gravitycode.solitaryfitness.util.android.disableLogcatThrottling
import com.gravitycode.solitaryfitness.util.android.enableStrictMode
import com.gravitycode.solitaryfitness.util.android.sdk
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.Executor
import javax.inject.Inject

class SolitaryFitnessApp : Application() {

    companion object {

        const val CRASH_ON_DEBUG_ERROR = false
    }

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