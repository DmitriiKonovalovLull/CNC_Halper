package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.domain.models.Operator
import kotlinx.coroutines.flow.Flow

interface OperatorRepository {
    fun getAllOperators(): Flow<List<OperatorEntity>>
    suspend fun getOperatorById(id: Long): OperatorEntity?
    suspend fun insertOperator(operator: Operator): Long // Renamed saveOperator to insertOperator
    suspend fun updateOperator(operator: Operator)
    suspend fun deleteOperator(id: Long)
    suspend fun getOperatorCount(): Int

    fun getOperator(): Flow<Operator?>
}