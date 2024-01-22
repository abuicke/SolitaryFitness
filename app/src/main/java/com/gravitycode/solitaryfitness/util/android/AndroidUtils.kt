@file:SuppressLint("ObsoleteSdkInt")

package com.gravitycode.solitaryfitness.util.android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import com.gravitycode.solitaryfitness.BuildConfig

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
 * Enable all strict mode policies for both [ThreadPolicy] and
 * [VmPolicy] that are available for the current minimum API level.
 *
 * NOTE: Needs to be updated with each new API release as long as (this bug)[https://issuetracker.google.com/issues/298834690]
 * exists and [VmPolicy.Builder.detectAll] cannot simply be used. If the aforementioned bug is fixed then
 * switch to [VmPolicy.Builder.detectAll] in the implementation.
 * */
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
fun enableStrictMode() {
    StrictMode.setThreadPolicy(
        ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDialog()
            .build()
    )

    StrictMode.setVmPolicy(
        VmPolicy.Builder().apply {
            detectLeakedSqlLiteObjects()
            sdk(Build.VERSION_CODES.HONEYCOMB) {
                detectActivityLeaks()
                // Disabled due to this bug https://issuetracker.google.com/issues/298834690
                // detectLeakedClosableObjects()
            }
            sdk(Build.VERSION_CODES.JELLY_BEAN) {
                detectLeakedRegistrationObjects()
            }
            sdk(Build.VERSION_CODES.JELLY_BEAN_MR2) {
                detectFileUriExposure()
            }
            sdk(Build.VERSION_CODES.M) {
                detectCleartextNetwork()
            }
            sdk(Build.VERSION_CODES.O) {
                detectUntaggedSockets()
                detectContentUriWithoutPermission()
            }
            sdk(Build.VERSION_CODES.P) {
                detectNonSdkApiUsage()
            }
            sdk(Build.VERSION_CODES.Q) {
                detectCredentialProtectedWhileLocked()
                detectImplicitDirectBoot()
            }
            sdk(Build.VERSION_CODES.S) {
                detectIncorrectContextUse()
                detectUnsafeIntentLaunch()
            }
            penaltyLog()
            penaltyDeath()
        }.build()
    )
}