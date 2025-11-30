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

    suspend fun processQuery(message: String, preferCloud: Boolean = false): AIResponse {
        return if (preferCloud) {
            try {
                cloudAPI.processWithCloudAI(message)
            } catch (e: Exception) {
                // Fallback to local AI if cloud fails
                miniAIEngine.processQuery(message)
            }
        } else {
            // Use local AI first, then cloud if confidence is low
            val localResponse = miniAIEngine.processQuery(message)
            if (((localResponse as? AIResponse.Success)?.confidence ?: 0f) < 0.7f) {
                try {
                    cloudAPI.processWithCloudAI(message)
                } catch (e: Exception) {
                    localResponse
                }
            } else {
                localResponse
            }
        }
    }

    fun getAvailableModels(): List<AIModelType> {
        return listOf(
            AIModelType.MiniTFLite,
            AIModelType.CloudGPT,
            AIModelType.CloudGPT4
        )
    }
}