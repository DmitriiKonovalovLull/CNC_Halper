package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.core.ai.AIServiceManager
import com.konchak.cnc_halper.core.ai.MiniAIEngine
import com.konchak.cnc_halper.data.local.database.dao.AIKnowledgeDao
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
            // 1. Сначала ищем в локальной базе знаний
            val localKnowledge = aiKnowledgeDao.findSimilar(message)

            if (localKnowledge.isNotEmpty()) {
                val bestMatch = localKnowledge.maxByOrNull { it.confidence }
                return AIResponse.Success(
                    answer = bestMatch?.answer ?: "Не нашел точного ответа",
                    confidence = bestMatch?.confidence ?: 0.7f,
                    source = "knowledge_base",
                    modelUsed = com.konchak.cnc_halper.domain.models.ai.AIModelType.Hybrid, // ИЗМЕНИЛ: было "local_knowledge_base"
                    processingTime = 0L,
                    requiresSync = false
                )
            }

            // 2. Если не нашли в базе - используем существующую логику
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
        // TODO: Реализовать получение модели
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
        // TODO: Реализовать обновление модели
        return true
    }

    override suspend fun syncAIModels(): Boolean {
        // TODO: Реализовать синхронизацию моделей
        return true
    }

    // НОВЫЙ МЕТОД: Обучение ИИ (сохранение знаний в базу)
    override suspend fun trainAI(question: String, answer: String, category: String) {
        try {
            // Создаем сущность для сохранения
            val knowledgeEntity = com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity(
                question = question,
                answer = answer,
                category = category,
                confidence = 1.0f,
                source = "operator_training"
            )

            // Сохраняем в базу
            aiKnowledgeDao.insert(knowledgeEntity)
        } catch (e: Exception) {
            throw Exception("Ошибка при обучении ИИ: ${e.message}")
        }
    }

    // НОВЫЙ МЕТОД: Получение статистики обучения
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

    // НОВЫЙ МЕТОД: Поиск похожих вопросов
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
            val allKnowledge = aiKnowledgeDao.getAll().first()
            allKnowledge.size
        } catch (_: Exception) {
            0
        }
    }

    override suspend fun knowsAnswer(question: String): Boolean {
        return try {
            val similar = aiKnowledgeDao.findSimilar(question)
            similar.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }
}