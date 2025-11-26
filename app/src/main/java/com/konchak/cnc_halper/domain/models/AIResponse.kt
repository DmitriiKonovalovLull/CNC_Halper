package com.konchak.cnc_halper.domain.models

import com.konchak.cnc_halper.domain.models.ai.AIModelType

data class AIResponse(
    val answer: String,
    val confidence: Float,
    val modelUsed: AIModelType,
    val processingTime: Long,
    val requiresSync: Boolean,
    val suggestions: List<String> = emptyList()
)