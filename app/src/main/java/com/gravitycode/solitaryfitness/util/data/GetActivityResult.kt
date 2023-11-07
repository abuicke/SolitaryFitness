package com.gravitycode.solitaryfitness.util.data

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import java.util.concurrent.atomic.AtomicInteger

class GetActivityResult<I, O>(
    activity: ComponentActivity,
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
) {

    private val nextLocalRequestCode = AtomicInteger()

    private val launcher = activity.activityResultRegistry.register<I, O>(
        "activity_rq#" + nextLocalRequestCode.getAndIncrement(), activity, contract, callback
    )

    operator fun invoke(i: I) {
        launcher.launch(i)
    }
}