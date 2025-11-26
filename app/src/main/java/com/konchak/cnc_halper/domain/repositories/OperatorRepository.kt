// domain/repositories/OperatorRepository.kt
package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import kotlinx.coroutines.flow.Flow

interface OperatorRepository {
    fun getAllOperators(): Flow<List<OperatorEntity>>
    suspend fun getOperatorById(id: Long): OperatorEntity?
    suspend fun saveOperator(name: String, workshop: String): Long
    suspend fun updateOperator(operator: OperatorEntity)
    suspend fun deleteOperator(id: Long)
    suspend fun getOperatorCount(): Int // ДОБАВИЛИ ЭТОТ МЕТОД
}