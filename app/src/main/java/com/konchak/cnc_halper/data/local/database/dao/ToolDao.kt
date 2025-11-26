// app/src/main/java/com/konchak/cnc_halper/data/local/database/dao/ToolDao.kt
package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Query("SELECT * FROM tools WHERE operator_id = :operatorId ORDER BY created_at DESC")
    fun getToolsByOperator(operatorId: Long): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE id = :id")
    suspend fun getToolById(id: Long): ToolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTool(tool: ToolEntity): Long

    @Update
    suspend fun updateTool(tool: ToolEntity)

    @Query("DELETE FROM tools WHERE id = :id")
    suspend fun deleteTool(id: Long)

    @Query("SELECT COUNT(*) FROM tools WHERE operator_id = :operatorId")
    suspend fun getToolsCount(operatorId: Long): Int

    @Query("SELECT * FROM tools ORDER BY created_at DESC")
    fun getAllTools(): Flow<List<ToolEntity>>
}