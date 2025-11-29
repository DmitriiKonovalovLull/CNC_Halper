package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Machine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor() : ViewModel() {

    private val _machines = MutableStateFlow<List<Machine>>(emptyList())
    val machines: StateFlow<List<Machine>> = _machines

    init {
        loadDemoMachines()
    }

    fun addNewMachine(machine: Machine) {
        viewModelScope.launch {
            _machines.value = _machines.value + machine
        }
    }

    private fun loadDemoMachines() {
        viewModelScope.launch(Dispatchers.IO) {
            _machines.value = listOf(
                Machine(
                    id = "machine_1",
                    name = "ЧПУ Фрезерный станок",
                    model = "DMG MORI NVX 5000",
                    status = "active",
                    serialNumber = "12345",
                    lastSync = System.currentTimeMillis(),
                    manufacturer = "DMG MORI",
                    year = 2020,
                    lastMaintenance = System.currentTimeMillis(),
                    nextMaintenance = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Machine(
                    id = "machine_2",
                    name = "Токарный станок",
                    model = "HAAS ST-20",
                    status = "idle",
                    serialNumber = "67890",
                    lastSync = System.currentTimeMillis(),
                    manufacturer = "HAAS",
                    year = 2018,
                    lastMaintenance = System.currentTimeMillis(),
                    nextMaintenance = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Machine(
                    id = "machine_3",
                    name = "5-осевой центр",
                    model = "MAZAK VARIAXIS",
                    status = "active",
                    serialNumber = "54321",
                    lastSync = System.currentTimeMillis(),
                    manufacturer = "MAZAK",
                    year = 2022,
                    lastMaintenance = System.currentTimeMillis(),
                    nextMaintenance = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}