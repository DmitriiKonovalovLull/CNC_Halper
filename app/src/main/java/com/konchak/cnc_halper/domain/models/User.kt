package com.konchak.cnc_halper.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val company: String? = null,
    val isActive: Boolean = true,
    val lastLogin: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    OPERATOR, TECHNOLOGIST, WORKSHOP_MASTER, ADMIN
}