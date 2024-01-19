package com.gravitycode.solitaryfitness.util.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

interface DataStoreManager {

    companion object {

        private val LOCK = Any()

        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(LOCK) {
                if (instance == null) {
                    instance = DataStoreManagerImpl(context)
                }
                instance!!
            }
        }
    }

    fun datastore(name: String): DataStore<Preferences>
}

private class DataStoreManagerImpl(private val context: Context) : DataStoreManager {

    private val datastores = mutableMapOf<String, DataStore<Preferences>>()

    override fun datastore(name: String): DataStore<Preferences> {
        return datastores.computeIfAbsent(name) {
            PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile(name)
            }
        }
    }
}