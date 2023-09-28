package com.gravitycode.simpletracker.app

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class SimpleTrackerApp : Application() {

    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()

}