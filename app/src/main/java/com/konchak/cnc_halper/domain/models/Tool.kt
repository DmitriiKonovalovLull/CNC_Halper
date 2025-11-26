package com.konchak.cnc_halper.domain.models

data class Tool(
    val id: String,
    val name: String,
    val type: String,
    val diameter: Float,
    val length: Float,
    val material: String,
    val wearLevel: Int = 0,
    val lastUsed: Long,
    val machineId: String,
    val imagePath: String? = null,
    val isActive: Boolean = true,
    val lastSync: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val operatorId: Long,
    val size: String,
    val photoPath: String?
)