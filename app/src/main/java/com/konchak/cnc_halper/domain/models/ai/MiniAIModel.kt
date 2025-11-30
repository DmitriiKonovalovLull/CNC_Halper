package com.konchak.cnc_halper.domain.models.ai

data class MiniAIModel(
    val id: String = "",
    val name: String = "",
    val version: String = "",
    val filePath: String = "",
    val accuracy: Float = 0f,
    val lastUpdated: Long = 0L,
    val sizeBytes: Long = 0L
)