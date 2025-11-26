package com.konchak.cnc_halper.domain.usecases.chat

import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.repositories.AIRepository
import javax.inject.Inject

class ProcessChatMessageUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(
        message: String,
        aiMode: AIMode
    ): AIResponse {
        return when (aiMode) {
            AIMode.CLOUD -> {
                aiRepository.processWithCloudAI(message)
            }
            AIMode.MINI -> {
                aiRepository.processWithMiniAI(message)
            }
            AIMode.HYBRID -> {
                aiRepository.processWithHybridAI(message)
            }
        }
    }
}

// Enum для использования в UseCase
enum class AIMode {
    CLOUD, MINI, HYBRID
}