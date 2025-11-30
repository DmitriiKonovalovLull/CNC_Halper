package com.konchak.cnc_halper.domain.usecases.ai

import com.konchak.cnc_halper.domain.models.ai.MiniAIModel // Add this import
import com.konchak.cnc_halper.domain.repositories.AIRepository
import javax.inject.Inject

class UpdateMiniAIModelUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(model: MiniAIModel): Boolean {
        return aiRepository.updateMiniAIModel(model)
    }
}
