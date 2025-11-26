package com.konchak.cnc_halper.core.ai

import com.konchak.cnc_halper.data.remote.api.AIService
import com.konchak.cnc_halper.data.remote.api.CloudAIRequest
import com.konchak.cnc_halper.data.remote.api.TrainingData
import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.AIModelType
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class CloudAPI @Inject constructor(
    private val aiService: AIService
) {

    @Suppress("unused")
    suspend fun processWithCloudAI(message: String): AIResponse {
        val startTime = System.currentTimeMillis()

        try {
            val response = aiService.chatWithCloudAI(
                CloudAIRequest(
                    message = message,
                    modelVersion = "gpt-4"
                )
            )

            if (response.isSuccessful) {
                val cloudResponse = response.body()!!
                return AIResponse(
                    answer = cloudResponse.answer,
                    confidence = cloudResponse.confidence,
                    modelUsed = AIModelType.CloudGPT4,
                    processingTime = System.currentTimeMillis() - startTime,
                    requiresSync = false
                )
            } else {
                throw Exception("Cloud AI service error: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Cloud AI unavailable: ${e.message}")
        }
    }

    @Suppress("unused")
    suspend fun getLatestAIModel(): MiniAIModel? {
        return try {
            val response = aiService.getLatestAIModel()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    @Suppress("unused")
    suspend fun syncTrainingData(data: TrainingData): Boolean {
        return try {
            val response = aiService.syncTrainingData(data)
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }
}