// domain/usecases/machines/GetMachinesUseCase.kt
package com.konchak.cnc_halper.domain.usecases.machines

import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.repositories.MachineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMachinesUseCase @Inject constructor(
    private val repository: MachineRepository
) {
    operator fun invoke(): Flow<List<Machine>> {
        return repository.getMachines()
    }
}