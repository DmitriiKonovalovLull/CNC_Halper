package com.konchak.cnc_halper.domain.models.ai

data class MiniAIModel(
    val id: String,
    val name: String,
    val version: String,
    val path: String,
    val accuracy: Float,
    val lastUpdated: Long,
    val modelSize: Long
)