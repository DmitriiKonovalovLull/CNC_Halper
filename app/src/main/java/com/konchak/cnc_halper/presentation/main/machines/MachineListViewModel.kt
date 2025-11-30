package com.konchak.cnc_halper.presentation.main.machines

import android.util.Log // Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.repositories.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val machineRepository: MachineRepository
) : ViewModel() {

    private val _machines = MutableStateFlow<List<Machine>>(emptyList())
    val machines: StateFlow<List<Machine>> = _machines

    private val TAG = "MachineListViewModel"

    init {
        loadMachines()
    }

    fun addNewMachine(machine: Machine) {
        viewModelScope.launch {
            Log.d(TAG, "addNewMachine: Adding new machine with ID: ${machine.id}")
            machineRepository.addMachine(machine)
        }
    }

    private fun loadMachines() {
        viewModelScope.launch(Dispatchers.IO) {
            machineRepository.getMachines().collectLatest { machinesList ->
                Log.d(TAG, "loadMachines: Loaded ${machinesList.size} machines.")
                _machines.value = machinesList
            }
        }
    }
}