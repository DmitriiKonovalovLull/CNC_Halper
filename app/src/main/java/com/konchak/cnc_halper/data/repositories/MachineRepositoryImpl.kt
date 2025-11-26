// data/repositories/MachineRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

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

    override fun getMachines(): Flow<List<Machine>> {
        return machineDao.getMachines().map { entities ->
            entities.map { MachineMapper.toDomain(it) }
        }
    }

    override suspend fun getMachineById(id: String): Machine? {
        return machineDao.getMachineById(id)?.let { MachineMapper.toDomain(it) }
    }

    override suspend fun addMachine(machine: Machine) {
        val entity = MachineMapper.toEntity(machine)
        machineDao.insertMachine(entity)
        // TODO: Add to offline cache for sync
    }

    override suspend fun updateMachine(machine: Machine) {
        val entity = MachineMapper.toEntity(machine)
        machineDao.updateMachine(entity)
    }

    override suspend fun deleteMachine(id: String) {
        machineDao.deleteMachine(id)
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