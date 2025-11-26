package com.konchak.cnc_halper.core.ai

import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.AIModelType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MiniAIEngine @Inject constructor() {
    private var currentModel: MiniAIModel = createDefaultModel()
    private var isInitialized: Boolean = false

    suspend fun initialize(): Boolean {
        return try {
            // Имитация инициализации TensorFlow Lite модели
            kotlinx.coroutines.delay(500)
            isInitialized = true
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun processQuery(query: String): AIResponse {
        if (!isInitialized) {
            initialize()
        }

        val startTime = System.currentTimeMillis()

        return try {
            val (answer, confidence) = processWithMiniAI(query)
            val processingTime = System.currentTimeMillis() - startTime

            AIResponse(
                answer = answer,
                confidence = confidence,
                modelUsed = AIModelType.MiniTFLite,
                processingTime = processingTime,
                requiresSync = true
            )
        } catch (_: Exception) {
            AIResponse(
                answer = "Мини-ИИ временно недоступен. Пожалуйста, проверьте подключение к интернету.",
                confidence = 0.0f,
                modelUsed = AIModelType.MiniTFLite,
                processingTime = System.currentTimeMillis() - startTime,
                requiresSync = false
            )
        }
    }

    fun getModelInfo(): MiniAIModel {
        return currentModel
    }

    suspend fun updateModel(newModel: MiniAIModel): Boolean {
        return try {
            // Имитация обновления модели
            kotlinx.coroutines.delay(1000)
            currentModel = newModel
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun createDefaultModel(): MiniAIModel {
        return MiniAIModel(
            id = "mini_ai_v1",
            name = "Mini AI v1",
            version = "1.1.0",
            path = "models/mini_ai.tflite",
            accuracy = 0.85f,
            lastUpdated = System.currentTimeMillis(),
            modelSize = (2.1 * 1024 * 1024).toLong()
        )
    }

    private fun processWithMiniAI(query: String): Pair<String, Float> {
        // Упрощенная логика мини-ИИ с базовыми знаниями
        val knowledgeBase = mapOf(
            "скорость резания алюминий" to
                    "Для алюминия: 180-280 м/мин. Подача 0.1-0.25 мм/зуб. Используйте острый инструмент.",
            "скорость резания сталь" to
                    "Для стали: 60-100 м/мин. Подача 0.05-0.12 мм/зуб. Применяйте СОЖ.",
            "износ инструмента признаки" to
                    "Признаки износа: ухудшение поверхности, вибрации, изменение стружки, наросты.",
            "фреза концевая параметры" to
                    "Концевая фреза: 2-4 зуба, скорость 80-200 м/мин в зависимости от материала.",
            "сож применение" to
                    "СОЖ обязательна для стали и титана. Для алюминия можно использовать воздух.",
            "материал обработка режимы" to
                    "Основные материалы: сталь (80-120), алюминий (200-300), титан (30-60) м/мин."
        )

        // Поиск наиболее подходящего ответа
        val bestMatch = knowledgeBase.entries
            .maxByOrNull { (key, _) -> calculateSimilarity(query, key) }

        return if (bestMatch != null && calculateSimilarity(query, bestMatch.key) > 0.3) {
            Pair(bestMatch.value, 0.75f + (calculateSimilarity(query, bestMatch.key) * 0.25f))
        } else {
            Pair(
                "Мини-ИИ: Для точных рекомендаций подключитесь к интернету для использования полной версии ИИ. " +
                        "Базовые рекомендации: проверьте остроту инструмента и используйте стандартные режимы для вашего материала.",
                0.65f
            )
        }
    }

    private fun calculateSimilarity(text1: String, text2: String): Float {
        val words1 = text1.lowercase().split(" ").toSet()
        val words2 = text2.lowercase().split(" ").toSet()
        val intersection = words1.intersect(words2)
        val union = words1.union(words2)
        return if (union.isEmpty()) 0.0f else intersection.size.toFloat() / union.size.toFloat()
    }
}