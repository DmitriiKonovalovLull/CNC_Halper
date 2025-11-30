package com.konchak.cnc_halper.domain.models

import com.konchak.cnc_halper.domain.models.ai.AIModelType

sealed class AIResponse {
    data class Success(
        val answer: String,
        val confidence: Float = 0.0f,
        val modelUsed: AIModelType? = null,
        val processingTime: Long = 0L,
        val requiresSync: Boolean = false
    ) : AIResponse()

    data class Error(val message: String) : AIResponse()
}