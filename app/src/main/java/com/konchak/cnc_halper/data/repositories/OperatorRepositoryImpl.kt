// app/src/main/java/com/konchak/cnc_halper/data/repositories/OperatorRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.OperatorDao
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import kotlinx.coroutines.flow.Flow
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

    override suspend fun saveOperator(name: String, workshop: String): Long {
        val operator = OperatorEntity(
            name = name,
            workshop = workshop
        )
        return operatorDao.insertOperator(operator)
    }

    override suspend fun updateOperator(operator: OperatorEntity) {
        operatorDao.updateOperator(operator)
    }

    override suspend fun deleteOperator(id: Long) {
        operatorDao.deleteOperator(id)
    }

    override suspend fun getOperatorCount(): Int {
        return operatorDao.getOperatorCount()
    }
}