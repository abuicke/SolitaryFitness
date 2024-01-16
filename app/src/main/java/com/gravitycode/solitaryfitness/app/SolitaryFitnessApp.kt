package com.gravitycode.solitaryfitness.app

import android.app.Application
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.di.ApplicationComponent
import com.gravitycode.solitaryfitness.di.DaggerApplicationComponent
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SolitaryFitnessApp : Application() {

    private val mutex = Mutex()

    private var applicationComponent: ApplicationComponent? = null

    suspend fun applicationComponent(activity: ComponentActivity): ApplicationComponent {
        return applicationComponent ?: mutex.withLock {
            applicationComponent ?: DaggerApplicationComponent.builder()
                .componentActivity(activity)
                .build().apply {
                    applicationComponent = this
                }
        }
    }
}