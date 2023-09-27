package com.gravitycode.simpletracker.util

fun intPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.intPreferencesKey(name.toString())

fun doublePreferencesKey(name: Any) =
    androidx.datastore.preferences.core.doublePreferencesKey(name.toString())

fun stringPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.stringPreferencesKey(name.toString())

fun booleanPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.booleanPreferencesKey(name.toString())

fun floatPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.floatPreferencesKey(name.toString())

fun longPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.longPreferencesKey(name.toString())

fun stringSetPreferencesKey(name: Any) =
    androidx.datastore.preferences.core.stringSetPreferencesKey(name.toString())