package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.AuthResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    suspend fun logout()
    fun getCurrentUserId(): String?
}