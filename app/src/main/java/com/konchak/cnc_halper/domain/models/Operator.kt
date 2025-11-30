package com.konchak.cnc_halper.domain.models

data class Operator(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val workshop: String = "",
    val shift: String = "day", // "day", "night", "mixed"
    val experience: Int = 0, // months
    val role: UserRole = UserRole.OPERATOR, // Use UserRole enum
    val createdAt: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis(),
    val stats: OperatorStats = OperatorStats()
) {
    @Suppress("unused")
    constructor() : this(
        id = "",
        name = "",
        email = "",
        workshop = "",
        shift = "day",
        experience = 0,
        role = UserRole.OPERATOR, // Use UserRole enum
        createdAt = System.currentTimeMillis(),
        lastActive = System.currentTimeMillis(),
        stats = OperatorStats()
    )
}

data class OperatorStats(
    val completedOperations: Int = 0,
    val toolsUsed: Int = 0,
    val qualityRate: Int = 0,
    val timeSaved: Int = 0
)
