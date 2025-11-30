package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.OperatorDao
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OperatorRepositoryImpl @Inject constructor(
    private val operatorDao: OperatorDao
) : OperatorRepository {

    override fun getAllOperators(): Flow<List<OperatorEntity>> {
        return operatorDao.getAllOperators()
    }

    override suspend fun getOperatorById(id: Long): OperatorEntity? {
        return operatorDao.getOperatorById(id)
    }

    override suspend fun insertOperator(operator: Operator): Long { // Renamed saveOperator to insertOperator
        return operatorDao.insertOperator(operator.toEntity()) // Используем маппер
    }

    override suspend fun updateOperator(operator: Operator) {
        operatorDao.updateOperator(operator.toEntity())
    }

    override suspend fun deleteOperator(id: Long) {
        operatorDao.deleteOperator(id)
    }

    override suspend fun getOperatorCount(): Int {
        return operatorDao.getOperatorCount()
    }

    override fun getOperator(): Flow<Operator?> {
        return operatorDao.getAllOperators().map { operators ->
            operators.firstOrNull()?.toDomain()
        }
    }
}