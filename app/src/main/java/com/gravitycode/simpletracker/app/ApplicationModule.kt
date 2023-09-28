package com.gravitycode.simpletracker.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module

@Module
class ApplicationModule {

    val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_history")
}