package com.gravitycode.solitaryfitness.util.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

fun megabytesToBytes(megabytes: Number) = megabytes.toLong() * 1024 * 1024

fun createPreferencesStoreFromFile(context: Context, name: String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(name)
    }
}