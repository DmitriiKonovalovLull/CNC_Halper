package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.Work
import kotlinx.coroutines.flow.Flow

interface WorkRepository {
    suspend fun createWork(work: Work): String
    suspend fun updateWork(work: Work)
    suspend fun deleteWork(workId: String)
    fun getWorkById(workId: String): Flow<Work?>
    fun getWorksByOperator(operatorId: String): Flow<List<Work>>
    fun getAllWorks(): Flow<List<Work>>
}