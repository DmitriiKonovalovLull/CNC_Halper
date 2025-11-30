package com.konchak.cnc_halper.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.EnergyMode
import com.konchak.cnc_halper.domain.models.SyncStatus
import com.konchak.cnc_halper.domain.repositories.OperatorRepository // Импортируем OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository // Внедряем OperatorRepository
) : ViewModel() {

    private val _operatorState = MutableStateFlow(OperatorState())
    val operatorState: StateFlow<OperatorState> = _operatorState

    private val _batteryLevel = MutableStateFlow(0)
    val batteryLevel: StateFlow<Int> = _batteryLevel

    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus

    val currentEnergyMode: EnergyMode = EnergyMode.STANDARD

    init {
        viewModelScope.launch {
            operatorRepository.getOperator().collect { operator ->
                _operatorState.value = _operatorState.value.copy(operator = operator)
            }
        }
        // TODO: Инициализация batteryLevel и syncStatus
    }

    fun manualSync() {
        // TODO: Implement manual sync
    }

    fun toggleBiometricAuth() {
        // TODO: Implement biometric auth toggle
    }
}

data class OperatorState(
    val operator: com.konchak.cnc_halper.domain.models.Operator? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)