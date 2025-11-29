package com.konchak.cnc_halper.domain.models

/**
 * Запись об одном использовании инструмента.
 * @param timestamp Время окончания операции.
 * @param duration Длительность операции в миллисекундах.
 * @param machineId ID станка, на котором проводилась операция.
 * @param finalWearLevel Уровень износа, зафиксированный оператором после операции.
 * @param notes Примечания оператора к этой конкретной операции.
 */
data class ToolUsageRecord(
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long,
    val machineId: String,
    val finalWearLevel: Int,
    val notes: String = ""
)
