package com.gravitycode.simpletracker.app

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class SimpleTrackerApp : Application() {

    lateinit var appComponent: ApplicationComponent
    /**
     * TODO: Move to Dagger
     * */
    val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_history")

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.create()
    }

}