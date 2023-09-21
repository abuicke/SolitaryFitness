package com.gravitycode.simpletracker.util

import timber.log.Timber
import java.util.EnumMap

@Deprecated("remove")
fun <K : Enum<K>, V : Any?> EnumMap<K, V>.withDefault(defaultValue: V): EnumMap<K, V> {

    Timber.d("keys = ${keys.size}")

    for (key in keys) {
        putIfAbsent(key, defaultValue)
        Timber.i("$key = ${get(key)}")
    }

//    replaceAll { _, v -> v ?: defaultValue }

    return this
}