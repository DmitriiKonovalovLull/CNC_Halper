package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    // === ОСНОВНЫЕ ОПЕРАЦИИ ===

    @Query("SELECT * FROM tools WHERE operatorId = :operatorId ORDER BY createdAt DESC")
    fun getToolsByOperator(operatorId: Long): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE id = :id")
    suspend fun getToolById(id: Long): ToolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTool(tool: ToolEntity): Long

    @Update
    suspend fun updateTool(tool: ToolEntity)

    @Query("DELETE FROM tools WHERE id = :id")
    suspend fun deleteTool(id: Long)

    @Query("SELECT COUNT(*) FROM tools WHERE operatorId = :operatorId")
    suspend fun getToolsCount(operatorId: Long): Int

    @Query("SELECT * FROM tools ORDER BY createdAt DESC")
    fun getAllTools(): Flow<List<ToolEntity>>

    // === СИНХРОНИЗАЦИЯ ===

    @Query("SELECT * FROM tools WHERE isSynced = 0")
    suspend fun getUnsyncedTools(): List<ToolEntity>

    @Query("UPDATE tools SET isSynced = :isSynced, lastSync = :lastSync WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, isSynced: Boolean, lastSync: Long)

    // === ДОПОЛНИТЕЛЬНЫЕ ЗАПРОСЫ ===

    @Query("SELECT * FROM tools WHERE status = :status ORDER BY createdAt DESC")
    fun getToolsByStatus(status: String): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE wearLevel >= :minWearLevel ORDER BY wearLevel DESC")
    fun getToolsByWearLevel(minWearLevel: Int): Flow<List<ToolEntity>>

    @Query("UPDATE tools SET wearLevel = :wearLevel WHERE id = :id")
    suspend fun updateWearLevel(id: Long, wearLevel: Int)

    @Query("UPDATE tools SET lastUsed = :timestamp, machineId = :machineId WHERE id = :id")
    suspend fun markToolUsed(id: Long, machineId: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM tools WHERE type = :type ORDER BY createdAt DESC")
    fun getToolsByType(type: String): Flow<List<ToolEntity>>

    @Query("SELECT COUNT(*) FROM tools WHERE wearLevel >= 4 AND status = 'active'")
    suspend fun getWornToolsCount(): Int
}
