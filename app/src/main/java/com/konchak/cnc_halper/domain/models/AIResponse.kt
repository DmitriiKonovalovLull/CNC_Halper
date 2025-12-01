// 📁 domain/models/AIResponse.kt
package com.konchak.cnc_halper.domain.models

sealed class AIResponse {
    data class Success(
        val answer: String,
        val confidence: Float = 1.0f,
        val source: String = "ai_engine",

        // Используем значение из ТВОЕГО AIModelType:
        val modelUsed: com.konchak.cnc_halper.domain.models.ai.AIModelType =
            com.konchak.cnc_halper.domain.models.ai.AIModelType.MiniTFLite, // ← ИЗМЕНИ: было UNKNOWN
        val processingTime: Long = 0L,
        val requiresSync: Boolean = false
    ) : AIResponse()

    data class Error(
        val message: String
    ) : AIResponse()
}