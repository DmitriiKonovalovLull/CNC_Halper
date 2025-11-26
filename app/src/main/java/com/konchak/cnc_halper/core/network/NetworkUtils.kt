package com.konchak.cnc_halper.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(
    private val context: Context
) {
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Unknown)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _connectionType = MutableStateFlow(ConnectionType.UNKNOWN)
    val connectionType: StateFlow<ConnectionType> = _connectionType.asStateFlow()

    init {
        updateCurrentConnectionState()
    }

    fun isConnected(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                } ?: false
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.isConnected == true
            }
        } catch (e: SecurityException) {
            // Если нет разрешения, считаем что соединение есть
            true
        }
    }

    fun isWifiConnected(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    type == ConnectivityManager.TYPE_WIFI && isConnected
                } ?: false
            }
        } catch (e: SecurityException) {
            false
        }
    }

    fun updateCurrentConnectionState() {
        _connectionType.value = getConnectionType()
        _networkState.value = if (isConnected()) {
            NetworkState.Available
        } else {
            NetworkState.Unavailable
        }
    }

    private fun getConnectionType(): ConnectionType {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
                        else -> ConnectionType.UNKNOWN
                    }
                } ?: ConnectionType.NONE
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    when {
                        type == ConnectivityManager.TYPE_WIFI && isConnected -> ConnectionType.WIFI
                        type == ConnectivityManager.TYPE_MOBILE && isConnected -> ConnectionType.CELLULAR
                        type == ConnectivityManager.TYPE_ETHERNET && isConnected -> ConnectionType.ETHERNET
                        type == ConnectivityManager.TYPE_VPN && isConnected -> ConnectionType.VPN
                        isConnected -> ConnectionType.UNKNOWN
                        else -> ConnectionType.NONE
                    }
                } ?: ConnectionType.NONE
            }
        } catch (e: SecurityException) {
            ConnectionType.UNKNOWN
        }
    }

    fun canPerformHeavySync(): Boolean {
        return isWifiConnected()
    }

    fun shouldUseMiniAI(): Boolean {
        return !isWifiConnected()
    }

    fun hasGoodConnectionQuality(): Boolean {
        return isWifiConnected()
    }
}

sealed class NetworkState {
    object Available : NetworkState()
    object Unavailable : NetworkState()
    object Unknown : NetworkState()
}

enum class ConnectionType {
    WIFI, CELLULAR, ETHERNET, VPN, UNKNOWN, NONE
}