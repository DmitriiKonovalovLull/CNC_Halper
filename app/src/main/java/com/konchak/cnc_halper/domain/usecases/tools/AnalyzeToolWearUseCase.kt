package com.konchak.cnc_halper.domain.usecases.tools

import com.konchak.cnc_halper.domain.models.ToolAnalysisResult
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import javax.inject.Inject

class AnalyzeToolWearUseCase @Inject constructor(
    private val toolRepository: ToolRepository
) {
    suspend operator fun invoke(toolId: String): ToolAnalysisResult {
        return toolRepository.analyzeToolWear(toolId)
    }
}