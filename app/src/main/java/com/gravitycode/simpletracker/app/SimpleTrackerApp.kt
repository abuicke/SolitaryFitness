package com.gravitycode.simpletracker.app

import android.app.Application

class SimpleTrackerApp: Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.create()
    }

}