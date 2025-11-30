package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.WorkDao
import com.konchak.cnc_halper.data.local.database.mappers.WorkMapper
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.domain.repositories.WorkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    private val workDao: WorkDao
) : WorkRepository {

    override suspend fun createWork(work: Work): String {
        val workEntity = WorkMapper.toEntity(work)
        val newId = workDao.insertWork(workEntity)
        return newId.toString()
    }

    override suspend fun updateWork(work: Work) {
        val workEntity = WorkMapper.toEntity(work)
        workDao.updateWork(workEntity)
    }

    override suspend fun deleteWork(workId: String) {
        val workEntity = workDao.getWorkById(workId.toLong())
        workEntity?.let { workDao.deleteWork(it) }
    }

    override fun getWorkById(workId: String): Flow<Work?> = flow {
        val entity = workDao.getWorkById(workId.toLong())
        emit(entity?.let { WorkMapper.toDomain(it) })
    }

    override fun getWorksByOperator(operatorId: String): Flow<List<Work>> {
        return workDao.getWorksByOperator(operatorId).map { entities ->
            entities.map { WorkMapper.toDomain(it) }
        }
    }

    override fun getAllWorks(): Flow<List<Work>> {
        return workDao.getAllWorks().map { entities ->
            entities.map { WorkMapper.toDomain(it) }
        }
    }
}