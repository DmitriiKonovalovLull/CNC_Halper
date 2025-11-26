package com.konchak.cnc_halper.domain.models

data class MachineAccess(
    val id: String,
    val userId: String,
    val machineId: String,
    val accessLevel: AccessLevel,
    val grantedAt: Long,
    val expiresAt: Long? = null,
    val isActive: Boolean = true,
    val lastSync: Long = System.currentTimeMillis()
)

enum class AccessLevel {
    VIEW, OPERATE, MAINTAIN, ADMIN
}