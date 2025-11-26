package com.konchak.cnc_halper.domain.models

data class ToolAnalysisResult(
    val toolId: String,
    val wearLevel: Int,
    val confidence: Float,
    val recommendations: List<String>,
    val estimatedLife: Int, // в часах
    val wearPercentage: Float,
    val remainingLife: Int
)