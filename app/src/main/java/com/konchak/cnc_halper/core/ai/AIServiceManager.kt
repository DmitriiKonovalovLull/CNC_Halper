package com.konchak.cnc_halper.core.ai

import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ai.AIModelType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIServiceManager @Inject constructor(
    private val miniAIEngine: MiniAIEngine,
    private val cloudAPI: CloudAPI
) {

    @Suppress("unused")
    suspend fun processQuery(message: String, preferCloud: Boolean = false): AIResponse {
        return if (preferCloud) {
            try {
                cloudAPI.processWithCloudAI(message)
            } catch (_: Exception) {
                // Fallback to local AI if cloud fails
                miniAIEngine.processQuery(message)
            }
        } else {
            // Use local AI first, then cloud if confidence is low
            val localResponse = miniAIEngine.processQuery(message)
            if (localResponse.confidence < 0.7f) {
                try {
                    cloudAPI.processWithCloudAI(message)
                } catch (_: Exception) {
                    localResponse
                }
            } else {
                localResponse
            }
        }
    }

    @Suppress("unused")
    fun getAvailableModels(): List<AIModelType> {
        return listOf(
            AIModelType.MiniTFLite,
            AIModelType.CloudGPT,
            AIModelType.CloudGPT4
        )
    }
}