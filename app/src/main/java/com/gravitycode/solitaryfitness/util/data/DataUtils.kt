package com.gravitycode.solitaryfitness.util.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

fun megabytesToBytes(megabytes: Number) = megabytes.toLong() * 1024 * 1024

@Suppress("RedundantRequireNotNullCall")
fun createPreferencesStoreFromFile(context: Context, name: String): DataStore<Preferences> {
    // TODO: Shouldn't be necessary, but seems to be causing an issue.
    checkNotNull(context)
    Log.i("datastore", "PreferenceDataStoreFactory.create $name\n${Log.getStackTraceString(Throwable())}")
    return PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(name)
    }
}