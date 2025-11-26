package com.konchak.cnc_halper.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.models.ui.MachineUiModel
import com.konchak.cnc_halper.domain.usecases.machines.AddMachineUseCase
import com.konchak.cnc_halper.domain.usecases.machines.DeleteMachineUseCase
import com.konchak.cnc_halper.domain.usecases.machines.GetMachinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EquipmentSetupViewModel @Inject constructor(
    private val addMachineUseCase: AddMachineUseCase,
    private val deleteMachineUseCase: DeleteMachineUseCase,
    private val getMachinesUseCase: GetMachinesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EquipmentSetupState())
    val state: StateFlow<EquipmentSetupState> = _state

    init {
        loadMachines()
    }

    fun onEvent(event: EquipmentSetupEvent) {
        when (event) {
            is EquipmentSetupEvent.AddMachineNameChanged -> {
                _state.update {
                    it.copy(
                        addMachineState = it.addMachineState.copy(
                            name = event.name,
                            nameError = null
                        )
                    )
                }
            }
            is EquipmentSetupEvent.AddMachineModelChanged -> {
                _state.update {
                    it.copy(
                        addMachineState = it.addMachineState.copy(
                            model = event.model
                        )
                    )
                }
            }
            is EquipmentSetupEvent.AddMachineSerialNumberChanged -> {
                _state.update {
                    it.copy(
                        addMachineState = it.addMachineState.copy(
                            serialNumber = event.serialNumber
                        )
                    )
                }
            }
            EquipmentSetupEvent.SubmitAddMachine -> addMachine()
            is EquipmentSetupEvent.RemoveMachine -> {
                viewModelScope.launch {
                    deleteMachineUseCase(event.machineId)
                }
            }
            EquipmentSetupEvent.CompleteSetup -> {
                // Логика завершения настройки
            }
        }
    }

    private fun addMachine() {
        viewModelScope.launch {
            val currentState = _state.value.addMachineState

            if (currentState.name.isBlank()) {
                _state.update {
                    it.copy(
                        addMachineState = currentState.copy(
                            nameError = "Введите название станка"
                        )
                    )
                }
                return@launch
            }

            _state.update { it.copy(addMachineState = currentState.copy(isLoading = true)) }

            val machine = Machine(
                id = UUID.randomUUID().toString(),
                name = currentState.name,
                model = currentState.model,
                serialNumber = currentState.serialNumber,
                lastSync = System.currentTimeMillis(),
                manufacturer = "", // Добавьте значения по умолчанию или получите их из формы
                year = 0,
                status = "Active",
                lastMaintenance = 0L,
                nextMaintenance = 0L,
                updatedAt = System.currentTimeMillis()
            )

            try {
                addMachineUseCase(machine)
                // Форма сбросится автоматически через Flow
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        addMachineState = currentState.copy(
                            isLoading = false,
                            nameError = "Ошибка добавления станка"
                        )
                    )
                }
            }
        }
    }

    private fun loadMachines() {
        getMachinesUseCase()
            .onEach { machines ->
                _state.update { state ->
                    state.copy(
                        machines = machines.map { machine ->
                            MachineUiModel(
                                id = machine.id,
                                name = machine.name,
                                model = machine.model,
                                serialNumber = machine.serialNumber
                            )
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}

data class EquipmentSetupState(
    val machines: List<MachineUiModel> = emptyList(),
    val addMachineState: AddMachineState = AddMachineState()
)

data class AddMachineState(
    val name: String = "",
    val model: String = "",
    val serialNumber: String = "",
    val nameError: String? = null,
    val isLoading: Boolean = false
)

sealed class EquipmentSetupEvent {
    data class AddMachineNameChanged(val name: String) : EquipmentSetupEvent()
    data class AddMachineModelChanged(val model: String) : EquipmentSetupEvent()
    data class AddMachineSerialNumberChanged(val serialNumber: String) : EquipmentSetupEvent()
    object SubmitAddMachine : EquipmentSetupEvent()
    data class RemoveMachine(val machineId: String) : EquipmentSetupEvent()
    object CompleteSetup : EquipmentSetupEvent()
}