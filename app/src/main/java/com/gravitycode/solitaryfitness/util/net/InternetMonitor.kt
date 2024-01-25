package com.gravitycode.solitaryfitness.util.net

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.gravitycode.solitaryfitness.util.android.Log
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

    fun observe(): Flow<InternetState>
}

private class InternetMonitorImpl(
    private val applicationScope: CoroutineScope,
    private val connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback(), InternetMonitor {

    private val sharedFlow = MutableSharedFlow<InternetState>(1)

    init {
        applicationScope.launch(Dispatchers.IO) {
            val internetRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            val unavailableCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onUnavailable() {
                    // Doesn't get triggered via registerNetworkCallback
                    this@InternetMonitorImpl.onUnavailable()
                }
            }

            connectivityManager.requestNetwork(internetRequest, unavailableCallback, 5000)

            val networkCallback = this@InternetMonitorImpl
            val validatedInternetRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()

            connectivityManager.registerNetworkCallback(validatedInternetRequest, networkCallback)
        }
    }

    override fun observe() = sharedFlow

    override fun onAvailable(network: Network) {
        applicationScope.launch(Dispatchers.IO) {
            Log.d(TAG, "internet network available $network, checking connection...")
            Log.i(TAG, InternetState.CHECKING_CONNECTION.toString())
            sharedFlow.emit(InternetState.CHECKING_CONNECTION)
            if (isOnline(30000)) {
                Log.i(TAG, InternetState.CONNECTED_INTERNET.toString())
                sharedFlow.emit(InternetState.CONNECTED_INTERNET)
            } else {
                Log.i(TAG, InternetState.CONNECTED_NO_INTERNET.toString())
                sharedFlow.emit(InternetState.CONNECTED_NO_INTERNET)
            }
        }
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        applicationScope.launch {
            Log.i(TAG, InternetState.LOSING_CONNECTION.toString())
            sharedFlow.emit(InternetState.LOSING_CONNECTION)
        }
    }

    override fun onLost(network: Network) {
        applicationScope.launch {
            Log.i(TAG, InternetState.LOST_CONNECTION.toString())
            sharedFlow.emit(InternetState.LOST_CONNECTION)
        }
    }

    override fun onUnavailable() {
        applicationScope.launch {
            Log.i(TAG, InternetState.NO_CONNECTION.toString())
            sharedFlow.emit(InternetState.NO_CONNECTION)
        }
    }
}