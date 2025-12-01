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

    // НОВЫЕ МЕТОДЫ для обучения ИИ через базу знаний
    /**
     * Обучить ИИ новому знанию
     * @param question Вопрос/запрос от оператора
     * @param answer Правильный ответ
     * @param category Категория (turning, milling, drilling, etc.)
     */
    suspend fun trainAI(question: String, answer: String, category: String = "general")

    /**
     * Получить статистику обучения ИИ
     * @return Map с данными: total_examples, by_category, last_updated
     */
    suspend fun getTrainingStats(): Map<String, Any>

    /**
     * Найти похожие вопросы в базе знаний
     * @param query Поисковый запрос
     * @param limit Максимальное количество результатов
     * @return Список похожих вопросов
     */
    suspend fun findSimilarQuestions(query: String, limit: Int = 5): List<String>

    /**
     * Получить количество записей в базе знаний
     */
    suspend fun getKnowledgeBaseSize(): Int

    /**
     * Проверить, знает ли ИИ ответ на вопрос
     * @param question Вопрос для проверки
     * @return true если есть похожие знания в базе
     */
    suspend fun knowsAnswer(question: String): Boolean
}