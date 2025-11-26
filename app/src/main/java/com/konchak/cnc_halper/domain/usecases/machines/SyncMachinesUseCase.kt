package com.konchak.cnc_halper.domain.usecases.machines

import com.konchak.cnc_halper.domain.repositories.MachineRepository
import javax.inject.Inject

class SyncMachinesUseCase @Inject constructor(
    private val machineRepository: MachineRepository
) {
    suspend operator fun invoke(): Boolean {
        return machineRepository.syncMachines()
    }
}