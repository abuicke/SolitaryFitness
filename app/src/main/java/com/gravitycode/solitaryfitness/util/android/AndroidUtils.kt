@file:SuppressLint("ObsoleteSdkInt")

package com.gravitycode.solitaryfitness.util.android

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.os.strictmode.Violation
import android.util.Log
import androidx.annotation.RequiresApi
import com.gravitycode.solitaryfitness.BuildConfig
import java.util.concurrent.Executor

/**
 * Only run the selected code if in debug, i.e. [BuildConfig.DEBUG] is set to `true`.
 * */
fun debug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

fun sdk(sdk: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= sdk) {
        block()
    }
}

/**
 * If an app is too chatty, Logcat will drop logs.
 *
 * See: (Missing logs on Android logcat)[https://stackoverflow.com/questions/60570015/] and
 * (android logcat logs chatty module line expire message)[https://stackoverflow.com/questions/34587273/]
 * */
fun disableLogcatThrottling() {
    val pid = Process.myPid()
    val whiteList = "logcat -P '$pid'"
    Runtime.getRuntime().exec(whiteList).waitFor()
}

/**
 * Enable all available strict mode policies for both [ThreadPolicy] and [VmPolicy].
 *
 * Only throws an exception if the root package of this application is included in the
 * stacktrace, otherwise just prints the relevant stacktrace with log level [Log.ERROR].
 * */
@RequiresApi(Build.VERSION_CODES.P)
fun enableStrictMode(context: Context, listenerExecutor: Executor) {

    val penaltyListener: (Violation) -> Unit = { violation ->
        val violationOriginatesFromApp = violation.stackTrace.fold(true) { acc, stackTraceElement ->
            acc && stackTraceElement.toString().contains(context.packageName)
        }

        if (violationOriginatesFromApp) {
            throw violation
        } else {
            Log.e("policy violation", Log.getStackTraceString(violation))
        }
    }

    StrictMode.setThreadPolicy(
        ThreadPolicy.Builder()
            .detectAll()
            .penaltyListener(listenerExecutor, penaltyListener)
            .build()
    )

    StrictMode.setVmPolicy(
        VmPolicy.Builder()
            .penaltyListener(listenerExecutor, penaltyListener)
            .detectAll()
            .build()
    )
}

/**
 * All strict mode policies will be disabled while @param[block] executes.
 * */
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