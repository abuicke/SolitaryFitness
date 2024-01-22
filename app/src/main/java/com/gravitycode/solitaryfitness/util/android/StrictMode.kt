package com.gravitycode.solitaryfitness.util.android

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.gravitycode.solitaryfitness.BuildConfig

fun <T> temporarilyDisableStrictMode(block: () -> T): T {
    if (!BuildConfig.DEBUG) {
        return block()
    }

    val savedThreadPolicy = StrictMode.getThreadPolicy()
    val savedVmPolicy = StrictMode.getVmPolicy()

    StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
    // TODO: No permitAll() for VmPolicy.Builder. Not sure if this is the same thing.
    StrictMode.setVmPolicy(VmPolicy.Builder().build())

    val t = block()

    StrictMode.setThreadPolicy(savedThreadPolicy)
    StrictMode.setVmPolicy(savedVmPolicy)

    return t
}