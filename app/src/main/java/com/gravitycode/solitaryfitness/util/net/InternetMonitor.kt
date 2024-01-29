package com.gravitycode.solitaryfitness.util.net

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.gravitycode.solitaryfitness.util.android.Log
import com.gravitycode.solitaryfitness.util.android.sdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.ThreadSafe

private const val TAG = "InternetMonitor"

@ThreadSafe
interface InternetMonitor {

    companion object {

        private val LOCK = Any()

        @Volatile
        private var instance: InternetMonitor? = null

        fun getInstance(
            applicationScope: CoroutineScope,
            connectivityManager: ConnectivityManager
        ): InternetMonitor {
            return instance ?: synchronized(LOCK) {
                if (instance == null) {
                    instance = InternetMonitorImpl(applicationScope, connectivityManager)
                }

                instance!!
            }
        }
    }

    fun subscribe(): Flow<InternetState>
}

private class InternetMonitorImpl(
    private val applicationScope: CoroutineScope,
    private val connectivityManager: ConnectivityManager
) : InternetMonitor {

    private val sharedFlow = MutableSharedFlow<InternetState>(1)

    init {
        sdk(Build.VERSION_CODES.S) {
            val internetRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()

            val internetCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    applicationScope.launch(Dispatchers.IO) {
                        sharedFlow.emit(InternetState.CONNECTED_INTERNET)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {

                }

                override fun onLost(network: Network) {

                }

                override fun onUnavailable() {

                }
            }

            connectivityManager.requestNetwork(internetRequest, internetCallback, 5000)

            connectivityManager.registerDefaultNetworkCallback(
                InternetCallback(applicationScope, "DEF")
            )
        }
    }

    override fun subscribe() = sharedFlow

    private fun emit(state: InternetState) {
        Log.v(TAG, "emit($state)")
        val lastState = sharedFlow.replayCache[0]
        if (lastState != state) {
            applicationScope.launch(Dispatchers.IO) {
                sharedFlow.emit(state)
            }
        } else {
            Log.d(TAG) {
                "not emitting state $state as it matches the last emitted state from the replay cache, " +
                        "replay cache = ${sharedFlow.replayCache}"
            }
        }
    }
}