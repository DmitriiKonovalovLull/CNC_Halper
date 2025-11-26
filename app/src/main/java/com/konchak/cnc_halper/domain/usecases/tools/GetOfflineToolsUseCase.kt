package com.konchak.cnc_halper.domain.usecases.tools

import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOfflineToolsUseCase @Inject constructor(
    private val toolRepository: ToolRepository
) {
    operator fun invoke(): Flow<List<Tool>> {
        return toolRepository.getTools()
    }
}