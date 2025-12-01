package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.core.ai.AIServiceManager
import com.konchak.cnc_halper.core.ai.MiniAIEngine
import com.konchak.cnc_halper.data.local.database.dao.AIKnowledgeDao
import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity
import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import com.konchak.cnc_halper.domain.repositories.AIRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val aiServiceManager: AIServiceManager,
    private val miniAIEngine: MiniAIEngine,
    private val aiKnowledgeDao: AIKnowledgeDao
) : AIRepository {

    override suspend fun processWithHybridAI(message: String): AIResponse {
        return try {
            val localKnowledge = aiKnowledgeDao.findSimilar(message)
            if (localKnowledge.isNotEmpty()) {
                val bestMatch = localKnowledge.maxByOrNull { it.confidence }
                return AIResponse.Success(
                    answer = bestMatch?.answer ?: "Не нашел точного ответа",
                    confidence = bestMatch?.confidence ?: 0.7f,
                    source = "knowledge_base",
                    modelUsed = com.konchak.cnc_halper.domain.models.ai.AIModelType.Hybrid,
                    processingTime = 0L,
                    requiresSync = false
                )
            }
            aiServiceManager.processQuery(message)
        } catch (e: Exception) {
            AIResponse.Error(
                message = "Ошибка в репозитории AI: ${e.message ?: "Неизвестная ошибка"}"
            )
        }
    }

    override suspend fun processWithMiniAI(message: String): AIResponse {
        return try {
            aiServiceManager.processQuery(message, preferCloud = false)
        } catch (e: Exception) {
            AIResponse.Error(
                message = "Ошибка локального AI: ${e.message ?: "Неизвестная ошибка"}"
            )
        }
    }

    override suspend fun processWithCloudAI(message: String): AIResponse {
        return try {
            aiServiceManager.processQuery(message, preferCloud = true)
        } catch (e: Exception) {
            AIResponse.Error(
                message = "Ошибка облачного AI: ${e.message ?: "Неизвестная ошибка"}"
            )
        }
    }

    override suspend fun getMiniAIModel(): MiniAIModel? {
        return null
    }

    override suspend fun getModelInfo(): MiniAIModel? {
        return try {
            miniAIEngine.getModelInfo()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun updateMiniAIModel(model: MiniAIModel): Boolean {
        return true
    }

    override suspend fun syncAIModels(): Boolean {
        return true
    }

    override suspend fun trainAI(question: String, answer: String, category: String) {
        try {
            val knowledgeEntity = AIKnowledgeEntity(
                question = question,
                answer = answer,
                category = category,
                confidence = 1.0f,
                source = "operator_training"
            )
            aiKnowledgeDao.insert(knowledgeEntity)
        } catch (e: Exception) {
            throw Exception("Ошибка при обучении ИИ: ${e.message}")
        }
    }

    override suspend fun getTrainingStats(): Map<String, Any> {
        return try {
            val allKnowledge = aiKnowledgeDao.getAll().first()
            val count = allKnowledge.size
            val byCategory = allKnowledge.groupBy { it.category }.mapValues { it.value.size }
            mapOf(
                "total_examples" to count,
                "by_category" to byCategory,
                "last_updated" to System.currentTimeMillis()
            )
        } catch (_: Exception) {
            emptyMap()
        }
    }

    override suspend fun findSimilarQuestions(query: String, limit: Int): List<String> {
        return try {
            val results = aiKnowledgeDao.findSimilar(query).take(limit)
            results.map { it.question }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun getKnowledgeBaseSize(): Int {
        return try {
            aiKnowledgeDao.getAll().first().size
        } catch (_: Exception) {
            0
        }
    }

    override suspend fun knowsAnswer(question: String): Boolean {
        return try {
            aiKnowledgeDao.findSimilar(question).isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun getAllKnowledge(): List<AIKnowledgeEntity> {
        return try {
            aiKnowledgeDao.getAll().first()
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteKnowledge(knowledge: AIKnowledgeEntity) {
        try {
            aiKnowledgeDao.delete(knowledge)
        } catch (e: Exception) {
            throw Exception("Ошибка при удалении знания: ${e.message}")
        }
    }
}
