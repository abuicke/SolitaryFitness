package com.gravitycode.simpletracker

import android.app.Application
import timber.log.Timber

class SimpleTrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}