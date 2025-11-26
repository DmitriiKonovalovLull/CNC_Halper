package com.konchak.cnc_halper.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Suppress("unused")
    val networkStatus: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        // Установите начальное состояние
        val currentState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } ?: false
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }

        trySend(currentState)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
