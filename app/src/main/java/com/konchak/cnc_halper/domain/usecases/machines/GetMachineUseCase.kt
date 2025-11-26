package com.konchak.cnc_halper.domain.usecases.machines

import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.repositories.MachineRepository
import javax.inject.Inject

class GetMachineUseCase @Inject constructor(
    private val machineRepository: MachineRepository
) {
    suspend operator fun invoke(machineId: String): Machine? {
        return machineRepository.getMachineById(machineId)
    }
}