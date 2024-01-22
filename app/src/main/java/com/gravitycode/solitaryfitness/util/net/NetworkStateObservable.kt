package com.gravitycode.solitaryfitness.util.net

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.ThreadSafe

@ThreadSafe
interface NetworkStateObservable {

    companion object {

        private val LOCK = Any()

        /**
         *
         *
         *
         * TODO: Is this required?
         *
         *
         *
         *
         *
         * */
        @Volatile
        private var instance: NetworkStateObservable? = null

        fun getInstance(
            applicationScope: CoroutineScope,
            connectivityManager: ConnectivityManager
        ): NetworkStateObservable {
            return instance ?: synchronized(LOCK) {
                if (instance == null) {
                    instance = NetworkStateObservableImpl(applicationScope, connectivityManager)
                }

                instance!!
            }
        }
    }

    fun observe(): Flow<NetworkState>
}

/**
 *
 *
 * TODO: Should I be using any other of the methods in [ConnectivityManager.NetworkCallback]?
 *
 *
 *
 * */
private class NetworkStateObservableImpl(
    private val applicationScope: CoroutineScope,
    connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback(), NetworkStateObservable {

    private val sharedFlow = MutableSharedFlow<NetworkState>(1)

    init {
        connectivityManager.registerDefaultNetworkCallback(this)
    }

    override fun observe() = sharedFlow

    override fun onAvailable(network: Network) {
        applicationScope.launch(Dispatchers.IO) {
            if (isOnline()) {
                sharedFlow.emit(NetworkState.CONNECTED_INTERNET)
            } else {
                sharedFlow.emit(NetworkState.CONNECTED_NETWORK)
            }
        }
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        applicationScope.launch {
            sharedFlow.emit(NetworkState.LOSING_CONNECTION)
        }
    }

    override fun onLost(network: Network) {
        applicationScope.launch {
            sharedFlow.emit(NetworkState.LOST_CONNECTION)
        }
    }

    override fun onUnavailable() {
        applicationScope.launch {
            sharedFlow.emit(NetworkState.NO_CONNECTION)
        }
    }
}