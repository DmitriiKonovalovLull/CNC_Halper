package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity
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
    suspend fun trainAI(question: String, answer: String, category: String = "general")
    suspend fun getTrainingStats(): Map<String, Any>
    suspend fun findSimilarQuestions(query: String, limit: Int = 5): List<String>
    suspend fun getKnowledgeBaseSize(): Int
    suspend fun knowsAnswer(question: String): Boolean
    suspend fun getAllKnowledge(): List<AIKnowledgeEntity>
    suspend fun deleteKnowledge(knowledge: AIKnowledgeEntity)
}
