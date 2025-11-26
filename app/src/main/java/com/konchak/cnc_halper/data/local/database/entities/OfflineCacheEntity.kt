package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "offline_cache")
data class OfflineCacheEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "type")
    val type: String, // "tool_scan", "chat_message", "ai_analysis"

    @ColumnInfo(name = "data")
    val data: String, // JSON данные

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "sync_attempts")
    val syncAttempts: Int = 0
)