package com.konchak.cnc_halper.domain.models

/**
 * Domain-модель инструмента оператора
 * Используется в бизнес-логике приложения
 */
data class Tool(
    val id: String = "", // Firebase document ID или локальный ID
    val name: String = "",
    val type: ToolType = ToolType.OTHER, // Changed to ToolType enum
    val diameter: Float = 0f, // мм
    val length: Float = 0f, // мм
    val material: String = "", // "HSS", "Carbide", "Cobalt", "PCD"
    val coating: String = "", // "TiN", "TiAlN", "DLC", "None"
    val wearLevel: Int = 1, // 1-5 шкала (1=новый, 5=изношен)
    val lastUsed: Long = System.currentTimeMillis(),
    val machineId: String? = "", // ID станка где используется
    val operatorId: String = "", // Firebase user ID оператора
    val status: String = "active", // "active", "worn", "broken", "maintenance"
    val createdAt: Long = System.currentTimeMillis(),
    val lastSync: Long = System.currentTimeMillis(),
    val imageUrl: String? = null, // Firebase Storage URL
    val notes: String = "", // Примечания оператора
    val usageHistory: List<ToolUsageRecord> = emptyList() // ✅ ДОБАВЛЕНО
) {
    // Пустой конструктор для Firebase/Firestore
    constructor() : this(
        id = "",
        name = "",
        type = ToolType.OTHER, // Changed to ToolType enum
        diameter = 0f,
        length = 0f,
        material = "",
        coating = "",
        wearLevel = 1,
        lastUsed = System.currentTimeMillis(),
        machineId = "",
        operatorId = "",
        status = "active",
        createdAt = System.currentTimeMillis(),
        lastSync = System.currentTimeMillis(),
        imageUrl = null,
        notes = "",
        usageHistory = emptyList()
    )

    // Проверка что инструмент активен и готов к работе
    fun isAvailable(): Boolean {
        return status == "active" && wearLevel < 4
    }

    // Получить размер в формате "10x50"
    fun getSizeString(): String {
        return if (diameter > 0f && length > 0f) {
            "${diameter}x${length}"
        } else {
            "Не указан"
        }
    }

    // Получить статус износа текстом
    fun getWearStatus(): String {
        return when (wearLevel) {
            1 -> "Новый"
            2 -> "Незначительный износ"
            3 -> "Умеренный износ"
            4 -> "Сильный износ"
            5 -> "Критический износ"
            else -> "Не определен"
        }
    }

    fun withNewUsage(record: ToolUsageRecord): Tool {
        return this.copy(
            usageHistory = this.usageHistory + record,
            wearLevel = record.finalWearLevel,
            lastUsed = record.timestamp
        )
    }
}
