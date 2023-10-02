package com.gravitycode.simpletracker.app

import android.app.Application

class SimpleTrackerApp : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
}