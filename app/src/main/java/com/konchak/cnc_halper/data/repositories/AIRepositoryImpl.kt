package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.core.ai.AIServiceManager
import com.konchak.cnc_halper.core.ai.MiniAIEngine
import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import com.konchak.cnc_halper.domain.repositories.AIRepository
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val aiServiceManager: AIServiceManager,
    private val miniAIEngine: MiniAIEngine // ✅ ДОБАВЛЕНО для доступа к getModelInfo()
) : AIRepository {

    override suspend fun processWithHybridAI(message: String): AIResponse {
        return try {
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
            miniAIEngine.getModelInfo() // ✅ ДОБАВЛЕНО метод
        } catch (e: Exception) {
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
}