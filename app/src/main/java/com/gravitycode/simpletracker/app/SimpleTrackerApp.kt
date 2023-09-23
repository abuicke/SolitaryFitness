package com.gravitycode.simpletracker.app

import android.app.Application
import com.gravitycode.simpletracker.BuildConfig
import timber.log.Timber

class SimpleTrackerApp: Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerApplicationComponent.create()
    }

}