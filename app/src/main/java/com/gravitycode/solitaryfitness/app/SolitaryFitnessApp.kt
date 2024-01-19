package com.gravitycode.solitaryfitness.app

import android.app.Application
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.SharedFlow

class SolitaryFitnessApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .build()
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