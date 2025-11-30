package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.WorkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(work: WorkEntity): Long

    @Update
    suspend fun updateWork(work: WorkEntity)

    @Delete
    suspend fun deleteWork(work: WorkEntity)

    @Query("SELECT * FROM works WHERE id = :workId")
    suspend fun getWorkById(workId: Long): WorkEntity?

    @Query("SELECT * FROM works WHERE operatorId = :operatorId ORDER BY startDate DESC")
    fun getWorksByOperator(operatorId: String): Flow<List<WorkEntity>>

    @Query("SELECT * FROM works ORDER BY startDate DESC")
    fun getAllWorks(): Flow<List<WorkEntity>>
}