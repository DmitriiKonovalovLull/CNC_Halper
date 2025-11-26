package com.konchak.cnc_halper.domain.usecases.tools

import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import javax.inject.Inject

class ScanToolUseCase @Inject constructor(
    private val toolRepository: ToolRepository
) {
    suspend operator fun invoke(imagePath: String): Tool {
        return toolRepository.scanTool(imagePath)
    }
}