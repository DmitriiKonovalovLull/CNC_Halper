// data/repositories/MachineRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

import android.util.Log // Import Log
import com.konchak.cnc_halper.data.local.database.dao.MachineDao
import com.konchak.cnc_halper.data.local.database.mappers.MachineMapper
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.repositories.MachineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MachineRepositoryImpl @Inject constructor(
    private val machineDao: MachineDao
) : MachineRepository {

    private val TAG = "MachineRepositoryImpl"

    override fun getMachines(): Flow<List<Machine>> {
        return machineDao.getMachines().map { entities ->
            entities.map {
                val domainMachine = MachineMapper.toDomain(it)
                Log.d(TAG, "getMachines: Mapped entity to domain machine with ID: ${domainMachine.id}")
                domainMachine
            }
        }
    }

    override suspend fun getMachineById(id: String): Machine? {
        val machine = machineDao.getMachineById(id)?.let { MachineMapper.toDomain(it) }
        Log.d(TAG, "getMachineById: Retrieved machine with ID: $id -> ${machine?.id}")
        return machine
    }

    override suspend fun addMachine(machine: Machine) {
        val entity = MachineMapper.toEntity(machine)
        machineDao.insertMachine(entity)
        Log.d(TAG, "addMachine: Added machine with ID: ${machine.id}")
        // TODO: Add to offline cache for sync
    }

    override suspend fun updateMachine(machine: Machine) {
        val entity = MachineMapper.toEntity(machine)
        machineDao.updateMachine(entity)
        Log.d(TAG, "updateMachine: Updated machine with ID: ${machine.id}")
    }

    override suspend fun deleteMachine(id: String) {
        machineDao.deleteMachine(id)
        Log.d(TAG, "deleteMachine: Deleted machine with ID: $id")
    }

    override suspend fun syncMachines(): Boolean {
        // TODO: Implement sync logic with conflict resolution
        return true
    }

    override suspend fun syncData(): Boolean {
        // TODO: Implement actual sync logic
        return true
    }
}