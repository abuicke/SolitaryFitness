package com.gravitycode.solitaryfitness.util.net

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
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
) : ConnectivityManager.NetworkCallback(), InternetMonitor {

    private val sharedFlow = MutableSharedFlow<InternetState>(1)

    init {
        applicationScope.launch(Dispatchers.IO) {
            val internetRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                // TODO: What does this do??
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
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
                // .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                // .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                // .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                // TODO: What does this do??
                // .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                .build()

            connectivityManager.registerNetworkCallback(validatedInternetRequest, networkCallback)
        }
    }

    override fun subscribe() = sharedFlow

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

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        Log.i(TAG, "onCapabilitiesChanged $network/$networkCapabilities")

        sdk(Build.VERSION_CODES.TIRAMISU) {
            val capabilities = networkCapabilities.capabilities.joinToString(",\n") { int ->
                "\t\t${networkCapabilityFromInt(int)}"
            }
            Log.d(TAG, "capabilities: {\n$capabilities\n}")
        }

        // networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        Log.i(TAG, "onBlockedStatusChanged $network/$blocked")
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun networkCapabilityFromInt(value: Int): String {
    return when (value) {
        0 -> "NET_CAPABILITY_MMS"
        1 -> "NET_CAPABILITY_SUPL"
        2 -> "NET_CAPABILITY_DUN"
        3 -> "NET_CAPABILITY_FOTA"
        4 -> "NET_CAPABILITY_IMS"
        5 -> "NET_CAPABILITY_CBS"
        6 -> "NET_CAPABILITY_WIFI_P2P"
        7 -> "NET_CAPABILITY_IA"
        8 -> "NET_CAPABILITY_RCS"
        9 -> "NET_CAPABILITY_XCAP"
        10 -> "NET_CAPABILITY_EIMS"
        11 -> "NET_CAPABILITY_NOT_METERED"
        12 -> "NET_CAPABILITY_INTERNET"
        13 -> "NET_CAPABILITY_NOT_RESTRICTED"
        14 -> "NET_CAPABILITY_TRUSTED"
        15 -> "NET_CAPABILITY_NOT_VPN"
        16 -> "NET_CAPABILITY_VALIDATED"
        17 -> "NET_CAPABILITY_CAPTIVE_PORTAL"
        18 -> "NET_CAPABILITY_NOT_ROAMING"
        19 -> "NET_CAPABILITY_FOREGROUND"
        20 -> "NET_CAPABILITY_NOT_CONGESTED"
        21 -> "NET_CAPABILITY_NOT_SUSPENDED"
        22 -> "NET_CAPABILITY_OEM_PAID"
        23 -> "NET_CAPABILITY_MCX"
        24 -> "NET_CAPABILITY_PARTIAL_CONNECTIVITY"
        25 -> "NET_CAPABILITY_TEMPORARILY_NOT_METERED"
        26 -> "NET_CAPABILITY_OEM_PRIVATE"
        27 -> "NET_CAPABILITY_VEHICLE_INTERNAL"
        28 -> "NET_CAPABILITY_NOT_VCN_MANAGED"
        29 -> "NET_CAPABILITY_ENTERPRISE"
        30 -> "NET_CAPABILITY_VSIM"
        31 -> "NET_CAPABILITY_BIP"
        32 -> "NET_CAPABILITY_HEAD_UNIT"
        33 -> "NET_CAPABILITY_MMTEL"
        34 -> "NET_CAPABILITY_PRIORITIZE_LATENCY"
        35 -> "NET_CAPABILITY_PRIORITIZE_BANDWIDTH"
        else -> throw IllegalArgumentException("Invalid net capability value: $value")
    }
}