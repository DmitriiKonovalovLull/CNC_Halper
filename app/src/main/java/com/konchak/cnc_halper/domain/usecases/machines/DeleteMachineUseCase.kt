package com.konchak.cnc_halper.domain.usecases.machines

import com.konchak.cnc_halper.domain.repositories.MachineRepository
import javax.inject.Inject

class DeleteMachineUseCase @Inject constructor(
    private val machineRepository: MachineRepository
) {
    suspend operator fun invoke(id: String) {
        machineRepository.deleteMachine(id)
    }
}