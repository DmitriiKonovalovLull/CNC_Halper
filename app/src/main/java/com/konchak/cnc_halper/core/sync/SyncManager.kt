package com.konchak.cnc_halper.core.sync

import com.konchak.cnc_halper.core.network.NetworkUtils
import com.konchak.cnc_halper.core.power.BatteryUtils
import com.konchak.cnc_halper.domain.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val batteryUtils: BatteryUtils,
    private val machineRepository: MachineRepository,
    private val aiRepository: AIRepository,
    private val toolRepository: ToolRepository,
    private val chatRepository: ChatRepository,
    private val offlineRepository: OfflineRepository
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var syncJob: Job? = null

    private val _syncState = MutableStateFlow(SyncState.IDLE)
    @Suppress("unused")
    val syncState: StateFlow<SyncState> = _syncState

    private val _lastSyncTime = MutableStateFlow<Long?>(null)
    @Suppress("unused")
    val lastSyncTime: StateFlow<Long?> = _lastSyncTime

    @Suppress("unused")
    fun startPeriodicSync() {
        syncJob?.cancel()
        syncJob = coroutineScope.launch {
            while (true) {
                if (shouldSync()) {
                    performSync()
                }
                delay(TimeUnit.MINUTES.toMillis(30))
            }
        }
    }

    @Suppress("unused")
    fun stopPeriodicSync() {
        syncJob?.cancel()
        syncJob = null
        _syncState.value = SyncState.IDLE
    }

    @Suppress("unused")
    suspend fun manualSync(): Boolean {
        return if (shouldSync()) {
            performSync()
        } else {
            false
        }
    }

    private suspend fun performSync(): Boolean {
        _syncState.value = SyncState.SYNCING

        return try {
            // Синхронизация всех данных
            val machinesSynced = machineRepository.syncData()
            val toolsSynced = toolRepository.syncData()
            val aiSynced = aiRepository.syncAIModels()
            val chatSynced = chatRepository.syncData()

            // Синхронизация офлайн кэша
            val offlineItems = offlineRepository.getUnsyncedItems().first()
            for (item in offlineItems) {
                // Логика синхронизации офлайн элементов
                offlineRepository.markItemAsSynced(item.id)
            }

            val success = machinesSynced && toolsSynced && aiSynced && chatSynced

            _syncState.value = if (success) {
                _lastSyncTime.value = System.currentTimeMillis()
                SyncState.SUCCESS
            } else {
                SyncState.ERROR
            }

            success
        } catch (_: Exception) {
            _syncState.value = SyncState.ERROR
            false
        } finally {
            coroutineScope.launch {
                delay(5000)
                if (_syncState.value != SyncState.SYNCING) {
                    _syncState.value = SyncState.IDLE
                }
            }
        }
    }

    fun shouldSync(): Boolean {
        return networkUtils.canPerformHeavySync() &&
                batteryUtils.canPerformBackgroundSync()
    }

    fun getSyncConditions(): SyncConditions {
        return SyncConditions(
            hasWifi = networkUtils.isWifiConnected(),
            isCharging = batteryUtils.isCharging(),
            batteryLevel = batteryUtils.getBatteryLevel(),
            isPowerSaveMode = batteryUtils.isPowerSaveMode(),
            lastSync = _lastSyncTime.value
        )
    }

    @Suppress("unused")
    fun forceSync() {
        coroutineScope.launch {
            performSync()
        }
    }

    @Suppress("unused")
    suspend fun getSyncStatus(): SyncStatus {
        val pendingCount = offlineRepository.getPendingSyncCount()
        return SyncStatus(
            lastSync = _lastSyncTime.value,
            pendingItems = pendingCount,
            syncConditions = getSyncConditions()
        )
    }
}

enum class SyncState {
    IDLE, SYNCING, SUCCESS, ERROR
}

data class SyncConditions(
    val hasWifi: Boolean,
    val isCharging: Boolean,
    val batteryLevel: Int,
    val isPowerSaveMode: Boolean,
    val lastSync: Long?
) {
    @Suppress("unused")
    val canSync: Boolean
        get() = hasWifi && isCharging && batteryLevel > 20 && !isPowerSaveMode
}

data class SyncStatus(
    val lastSync: Long?,
    val pendingItems: Int,
    val syncConditions: SyncConditions
)
