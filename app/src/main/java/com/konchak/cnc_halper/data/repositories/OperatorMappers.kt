package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.data.local.database.entities.OperatorStatsEntity
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.domain.models.OperatorStats
import com.konchak.cnc_halper.domain.models.UserRole

// Маппер из OperatorStatsEntity в доменную модель OperatorStats
fun OperatorStatsEntity.toDomain(): OperatorStats {
    return OperatorStats(
        completedOperations = this.completedOperations,
        toolsUsed = this.toolsUsed,
        qualityRate = this.qualityRate,
        timeSaved = this.timeSaved
    )
}

// Маппер из доменной модели OperatorStats в OperatorStatsEntity
fun OperatorStats.toEntity(): OperatorStatsEntity {
    return OperatorStatsEntity(
        completedOperations = this.completedOperations,
        toolsUsed = this.toolsUsed,
        qualityRate = this.qualityRate,
        timeSaved = this.timeSaved
    )
}

// Маппер из OperatorEntity в доменную модель Operator
fun OperatorEntity.toDomain(): Operator {
    return Operator(
        id = this.id.toString(),
        name = this.name,
        email = this.email,
        workshop = this.workshop,
        shift = this.shift,
        experience = this.experience,
        role = UserRole.valueOf(this.role), // Convert String to UserRole
        createdAt = this.createdAt,
        lastActive = this.lastUpdated,
        stats = this.stats.toDomain() // Маппим встроенные stats
    )
}

// Маппер из доменной модели Operator в OperatorEntity
fun Operator.toEntity(): OperatorEntity {
    return OperatorEntity(
        id = this.id.toLongOrNull() ?: 0L,
        name = this.name,
        email = this.email,
        workshop = this.workshop,
        shift = this.shift,
        experience = this.experience,
        role = this.role.name, // Convert UserRole to String
        createdAt = this.createdAt,
        lastUpdated = System.currentTimeMillis(),
        stats = this.stats.toEntity() // Маппим встроенные stats
    )
}
