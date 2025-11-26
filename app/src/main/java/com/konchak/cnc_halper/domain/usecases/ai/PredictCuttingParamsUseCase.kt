package com.konchak.cnc_halper.domain.usecases.ai

import com.konchak.cnc_halper.domain.models.CuttingParameters
import com.konchak.cnc_halper.domain.repositories.AIRepository
import javax.inject.Inject

class PredictCuttingParamsUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(
        material: String,
        toolType: String,
        operation: String
    ): CuttingParameters {
        val query = "Рекомендуй параметры резания для: материал=$material, инструмент=$toolType, операция=$operation"
        val response = aiRepository.processWithHybridAI(query)

        return parseCuttingParamsFromResponse(response.answer)
    }

    private fun parseCuttingParamsFromResponse(response: String): CuttingParameters {
        // Парсинг ответа ИИ в структурированные данные
        return CuttingParameters(
            cuttingSpeed = extractValue(response, "скорость резания", 100f),
            feedRate = extractValue(response, "подача", 0.1f),
            depthOfCut = extractValue(response, "глубина резания", 1.0f),
            recommendations = extractRecommendations(response)
        )
    }

    private fun extractValue(text: String, key: String, defaultValue: Float): Float {
        val pattern = "$key[\\s:]*([0-9]+(?:\\.[0-9]+)?)".toRegex(RegexOption.IGNORE_CASE)
        return pattern.find(text)?.groupValues?.get(1)?.toFloatOrNull() ?: defaultValue
    }

    private fun extractRecommendations(text: String): List<String> {
        return text.split("\n", ". ")
            .filter { it.contains("рекоменд", ignoreCase = true) || it.length > 20 }
            .take(3)
    }
}