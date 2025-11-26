package com.konchak.cnc_halper.domain.models

data class OfflineCacheItem(
    val id: String,
    val type: CacheType,
    val data: String, // JSON данные
    val createdAt: Long,
    val isSynced: Boolean = false,
    val syncAttempts: Int = 0
)

enum class CacheType {
    TOOL_SCAN, CHAT_MESSAGE, AI_ANALYSIS, MACHINE_UPDATE
}