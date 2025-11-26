package com.konchak.cnc_halper.domain.models

import com.konchak.cnc_halper.core.network.ConnectionType
import com.konchak.cnc_halper.core.network.NetworkState
import com.konchak.cnc_halper.core.network.NetworkUtils
import com.konchak.cnc_halper.domain.models.ai.AIModelType

@Suppress("unused")
data class HybridAIState(
    val isOnline: Boolean,
    val currentModel: AIModelType,
    val miniModelAccuracy: Float,
    val lastSyncTime: Long,
    val pendingSyncCount: Int,
    val cloudAIAvailable: Boolean,
    val miniAIAvailable: Boolean,
    val lastCloudResponseTime: Long,
    val lastMiniResponseTime: Long,
    val syncConditions: SyncConditions
) {
    val shouldUseCloudAI: Boolean
        get() = isOnline && cloudAIAvailable && syncConditions.canSync

    val shouldUseMiniAI: Boolean
        get() = !isOnline || !cloudAIAvailable || !syncConditions.canSync

    val canSwitchToCloud: Boolean
        get() = isOnline && cloudAIAvailable

    val modelEffectiveness: Float
        get() = when (currentModel) {
            AIModelType.CloudGPT4 -> 1.0f
            AIModelType.MiniTFLite -> miniModelAccuracy
            AIModelType.Hybrid -> 0.9f
            else -> 0.0f
        }
}

data class SyncConditions(
    val hasWifi: Boolean,
    val isCharging: Boolean,
    val batteryLevel: Int,
    val isPowerSaveMode: Boolean
) {
    val canSync: Boolean
        get() = hasWifi && isCharging && batteryLevel > 20 && !isPowerSaveMode

    val syncPriority: Int
        get() = when {
            !canSync -> 0
            batteryLevel > 80 && hasWifi -> 3
            batteryLevel > 50 && hasWifi -> 2
            else -> 1
        }
}

@Suppress("unused")
class HybridAIManager(
    private val networkUtils: NetworkUtils
) {
    fun createHybridAIState(
        currentModel: AIModelType,
        miniModelAccuracy: Float,
        lastSyncTime: Long,
        pendingSyncCount: Int,
        cloudAIAvailable: Boolean,
        miniAIAvailable: Boolean,
        lastCloudResponseTime: Long,
        lastMiniResponseTime: Long,
        isCharging: Boolean,
        batteryLevel: Int,
        isPowerSaveMode: Boolean
    ): HybridAIState {
        val isOnline = networkUtils.isConnected()
        val hasWifi = networkUtils.isWifiConnected()

        val syncConditions = SyncConditions(
            hasWifi = hasWifi,
            isCharging = isCharging,
            batteryLevel = batteryLevel,
            isPowerSaveMode = isPowerSaveMode
        )

        return HybridAIState(
            isOnline = isOnline,
            currentModel = currentModel,
            miniModelAccuracy = miniModelAccuracy,
            lastSyncTime = lastSyncTime,
            pendingSyncCount = pendingSyncCount,
            cloudAIAvailable = cloudAIAvailable,
            miniAIAvailable = miniAIAvailable,
            lastCloudResponseTime = lastCloudResponseTime,
            lastMiniResponseTime = lastMiniResponseTime,
            syncConditions = syncConditions
        )
    }

    fun getOptimalModelType(): AIModelType {
        return when {
            networkUtils.isConnected() && networkUtils.isWifiConnected() &&
                    !networkUtils.shouldUseMiniAI() -> AIModelType.CloudGPT4
            networkUtils.isConnected() -> AIModelType.Hybrid
            else -> AIModelType.MiniTFLite
        }
    }

    fun shouldUseMiniAI(): Boolean {
        return networkUtils.shouldUseMiniAI()
    }

    fun canPerformHeavySync(): Boolean {
        return networkUtils.canPerformHeavySync()
    }

    fun hasGoodConnectionQuality(): Boolean {
        return networkUtils.hasGoodConnectionQuality()
    }

    fun getCurrentConnectionType(): ConnectionType {
        return networkUtils.connectionType.value
    }

    fun getCurrentNetworkState(): NetworkState {
        return networkUtils.networkState.value
    }

    fun calculateSyncPriority(
        isCharging: Boolean,
        batteryLevel: Int,
        isPowerSaveMode: Boolean
    ): Int {
        val hasWifi = networkUtils.isWifiConnected()
        val syncConditions = SyncConditions(
            hasWifi = hasWifi,
            isCharging = isCharging,
            batteryLevel = batteryLevel,
            isPowerSaveMode = isPowerSaveMode
        )
        return syncConditions.syncPriority
    }
}