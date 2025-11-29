package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class ToolEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Основные поля
    val operatorId: Long,
    val name: String,
    val type: String,
    val size: String,
    val photoPath: String? = null,

    // Технические характеристики
    val diameter: Float = 0f,
    val length: Float = 0f,
    val material: String = "",
    val coating: String = "",

    // Состояние инструмента
    val wearLevel: Int = 1, // 1-5 шкала износа
    val status: String = "active", // active, worn, broken, maintenance
    val lastUsed: Long = System.currentTimeMillis(),
    val machineId: String? = "",
    val notes: String = "",

    // Системные поля
    val createdAt: Long = System.currentTimeMillis(),
    val lastSync: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val usageHistory: String = "" // ✅ ДОБАВЛЕНО: JSON-строка с историей
)