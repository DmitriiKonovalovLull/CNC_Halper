package com.konchak.cnc_halper.domain.models

/**
 * Domain-модель для "Работы" или "Проекта".
 * Представляет собой отдельную производственную задачу или проект,
 * который оператор выполняет на станке.
 */
data class Work(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val machineId: String? = null,
    val toolIds: List<String> = emptyList(),
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val status: WorkStatus = WorkStatus.IN_PROGRESS,
    val operatorId: String = "",
    val isGostProject: Boolean = false, // New field for GOST projects
    val gostType: String? = null // New field for GOST type
)

enum class WorkStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    PAUSED,
    CANCELLED
}