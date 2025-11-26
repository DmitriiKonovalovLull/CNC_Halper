package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.usecases.machines.GetMachineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineDetailViewModel @Inject constructor(
    private val getMachineUseCase: GetMachineUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MachineDetailState())
    val state: StateFlow<MachineDetailState> = _state

    fun onEvent(event: MachineDetailEvent) {
        when (event) {
            is MachineDetailEvent.LoadMachine -> loadMachine(event.machineId)
        }
    }

    private fun loadMachine(machineId: String) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val machine = getMachineUseCase(machineId)
                _state.value = _state.value.copy(
                    isLoading = false,
                    machine = machine
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }
}

data class MachineDetailState(
    val machine: Machine? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MachineDetailEvent {
    data class LoadMachine(val machineId: String) : MachineDetailEvent()
}