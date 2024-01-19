package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.BuildConfig
import kotlinx.coroutines.flow.SharedFlow


class SolitaryFitnessApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        enableStrictModeIfDebug()
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

    private fun enableStrictModeIfDebug() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .build()
            )

            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}