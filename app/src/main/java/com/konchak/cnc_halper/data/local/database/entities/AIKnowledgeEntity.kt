// 📁 data/local/database/entities/AIKnowledgeEntity.kt
package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ai_knowledge")
data class AIKnowledgeEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    // Вопрос от оператора
    val question: String,

    // Правильный ответ
    val answer: String,

    // Категория: turning, milling, drilling
    val category: String = "general",

    // Уверенность ИИ в этом ответе (0.0 - 1.0)
    val confidence: Float = 1.0f,

    // Источник: operator, manual, calculated
    val source: String = "operator",

    // Когда добавлено
    val createdAt: Long = System.currentTimeMillis(),

    // Количество использований
    val usageCount: Int = 0,

    // Было ли подтверждено оператором
    val verified: Boolean = false
)