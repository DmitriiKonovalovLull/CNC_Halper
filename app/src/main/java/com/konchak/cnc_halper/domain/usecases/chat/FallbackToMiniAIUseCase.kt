package com.konchak.cnc_halper.domain.usecases.chat

import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.repositories.AIRepository
import javax.inject.Inject

class FallbackToMiniAIUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(originalMessage: String, @Suppress("unused") cloudError: Exception): AIResponse {
        return aiRepository.processWithMiniAI(originalMessage)
    }
}