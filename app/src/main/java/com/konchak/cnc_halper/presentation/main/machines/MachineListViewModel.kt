package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.core.sync.SyncManager
import com.konchak.cnc_halper.domain.models.ui.MachineUiModel
import com.konchak.cnc_halper.domain.usecases.machines.GetMachinesUseCase
import com.konchak.cnc_halper.domain.usecases.machines.SyncMachinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val getMachinesUseCase: GetMachinesUseCase,
    private val syncMachinesUseCase: SyncMachinesUseCase,
    private val syncManager: SyncManager // Добавляем SyncManager
) : ViewModel() {

    private val _state = MutableStateFlow(MachineListState())
    val state: StateFlow<MachineListState> = _state

    init {
        loadMachines()
        observeSyncConditions()
    }

    fun onEvent(event: MachineListEvent) {
        when (event) {
            MachineListEvent.Refresh -> loadMachines()
            MachineListEvent.SyncMachines -> syncMachines()
        }
    }

    private fun loadMachines() {
        _state.update { it.copy(isLoading = true) }

        getMachinesUseCase()
            .onEach { machines ->
                _state.update { state ->
                    state.copy(
                        machines = machines.map { machine ->
                            MachineUiModel(
                                id = machine.id,
                                name = machine.name,
                                model = machine.model,
                                serialNumber = machine.serialNumber,
                                lastSync = machine.lastSync
                            )
                        },
                        isLoading = false
                    )
                }
                // Обновляем статус синхронизации после загрузки станков
                updateSyncStatus()
            }
            .launchIn(viewModelScope)
    }

    private fun syncMachines() {
        viewModelScope.launch {
            if (!syncManager.shouldSync()) {
                _state.update { state ->
                    state.copy(
                        syncStatus = MachineSyncStatus.Error(
                            "Невозможно синхронизировать: проверьте подключение к Wi-Fi и уровень заряда батареи"
                        )
                    )
                }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            try {
                val success = syncMachinesUseCase()
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        syncStatus = if (success) {
                            MachineSyncStatus.Success(state.machines.size)
                        } else {
                            MachineSyncStatus.Error("Не удалось синхронизировать станки")
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        syncStatus = MachineSyncStatus.Error(e.message ?: "Ошибка синхронизации")
                    )
                }
            }
        }
    }

    private fun observeSyncConditions() {
        viewModelScope.launch {
            // Можно добавить наблюдение за изменениями условий синхронизации
            updateSyncStatus()
        }
    }

    private suspend fun updateSyncStatus() {
        val syncStatus = syncManager.getSyncStatus()
        val pendingCount = syncStatus.pendingItems

        _state.update { state ->
            when {
                pendingCount > 0 -> state.copy(
                    syncStatus = MachineSyncStatus.Pending(pendingCount)
                )
                syncStatus.lastSync != null -> state.copy(
                    syncStatus = MachineSyncStatus.Success(state.machines.size)
                )
                else -> state.copy(
                    syncStatus = null
                )
            }
        }
    }
}

data class MachineListState(
    val machines: List<MachineUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val syncStatus: MachineSyncStatus? = null // Переименовываем чтобы избежать конфликта
)

sealed class MachineListEvent {
    object Refresh : MachineListEvent()
    object SyncMachines : MachineListEvent()
}

// Переименовываем чтобы избежать конфликта с SyncManager.SyncStatus
sealed class MachineSyncStatus {
    data class Success(val syncedCount: Int) : MachineSyncStatus()
    data class Error(val message: String) : MachineSyncStatus()
    data class Pending(val pendingCount: Int) : MachineSyncStatus()
}