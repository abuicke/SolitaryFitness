package com.gravitycode.simpletracker.app

import android.app.Application

class SimpleTrackerApp : Application() {

    /**
     * TODO: Should be able to add application context through component builder DaggerApplicationComponent.context(this).create()
     * */
    val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()

}