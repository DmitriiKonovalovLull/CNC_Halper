package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel

interface AIRepository {
    suspend fun processWithHybridAI(message: String): AIResponse
    suspend fun processWithMiniAI(message: String): AIResponse
    suspend fun processWithCloudAI(message: String): AIResponse
    suspend fun getMiniAIModel(): MiniAIModel?
    suspend fun getModelInfo(): MiniAIModel?
    suspend fun updateMiniAIModel(model: MiniAIModel): Boolean
    suspend fun syncAIModels(): Boolean
}