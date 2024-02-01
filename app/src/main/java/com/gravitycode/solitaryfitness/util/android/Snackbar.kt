package com.gravitycode.solitaryfitness.util.android

import com.gravitycode.solitaryfitness.util.error
import kotlinx.coroutines.runBlocking

const val SHORT_DURATION_MILLIS = 3000L
const val LONG_DURATION_MILLIS = 5000L

data class Snackbar(
    val message: String,
    val duration: SnackbarDuration,
    val action: SnackbarAction? = null
)

enum class SnackbarDuration {

    /**
     * A short duration which last for [SHORT_DURATION_MILLIS] milliseconds
     * */
    SHORT {
        override suspend fun delay() {
            kotlinx.coroutines.delay(SHORT_DURATION_MILLIS)
        }
    },

    /**
     * A long duration which last for [LONG_DURATION_MILLIS] milliseconds
     * */
    LONG {
        override suspend fun delay() {
            kotlinx.coroutines.delay(LONG_DURATION_MILLIS)
        }
    },

    /**
     * An indefinite duration
     * */
    INDEFINITE {
        override suspend fun delay() {
            error("can't delay on ${toString()} duration") {
                /**
                 * TODO: This needs to be tested to make sure that by
                 *  recovering from an error I'm not making things worse.
                 * */
                runBlocking {
                    LONG.delay()
                }
            }
        }
    };

    /**
     * Calls [kotlinx.coroutines.delay] for the amount of time specified for each duration.
     *
     * @throws IllegalStateException If [delay] is called on [INDEFINITE].
     * */
    abstract suspend fun delay()
}

data class SnackbarAction(
    val text: String,
    val onClick: () -> Unit
)