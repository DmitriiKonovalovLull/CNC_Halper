// domain/repositories/MachineRepository.kt
package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.Machine
import kotlinx.coroutines.flow.Flow

interface MachineRepository {
    fun getMachines(): Flow<List<Machine>>
    suspend fun getMachineById(id: String): Machine?
    suspend fun addMachine(machine: Machine)
    suspend fun updateMachine(machine: Machine)
    suspend fun deleteMachine(id: String)
    suspend fun syncMachines(): Boolean
    suspend fun syncData(): Boolean
}