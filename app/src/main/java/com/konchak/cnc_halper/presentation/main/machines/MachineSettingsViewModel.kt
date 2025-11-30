package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.usecases.machines.GetMachineByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineSettingsViewModel @Inject constructor(
    private val getMachineByIdUseCase: GetMachineByIdUseCase
) : ViewModel() {

    private val _machine = MutableStateFlow<Machine?>(null)
    val machine: StateFlow<Machine?> = _machine

    private val _latheSpindleSpeed = MutableStateFlow("")
    val latheSpindleSpeed: StateFlow<String> = _latheSpindleSpeed

    private val _millingSpindleSpeed = MutableStateFlow("")
    val millingSpindleSpeed: StateFlow<String> = _millingSpindleSpeed

    private val _generalSpindleSpeed = MutableStateFlow("")
    val generalSpindleSpeed: StateFlow<String> = _generalSpindleSpeed

    fun loadMachine(machineId: String) {
        viewModelScope.launch {
            _machine.value = getMachineByIdUseCase(machineId)
            // TODO: Load existing settings for spindle speeds if available
        }
    }

    fun onLatheSpindleSpeedChanged(speed: String) {
        _latheSpindleSpeed.value = speed
    }

    fun onMillingSpindleSpeedChanged(speed: String) {
        _millingSpindleSpeed.value = speed
    }

    fun onGeneralSpindleSpeedChanged(speed: String) {
        _generalSpindleSpeed.value = speed
    }

    // TODO: Add save settings functionality
    fun saveSettings() {
        // Implement saving logic here, using the appropriate spindle speed(s) based on machine.type
    }
}
