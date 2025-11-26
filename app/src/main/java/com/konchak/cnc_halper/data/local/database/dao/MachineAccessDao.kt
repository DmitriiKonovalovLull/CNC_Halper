package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.MachineAccessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineAccessDao {
    @Query("SELECT * FROM machine_access WHERE user_id = :userId AND is_active = 1")
    fun getUserMachineAccess(userId: String): Flow<List<MachineAccessEntity>>

    @Query("SELECT * FROM machine_access WHERE machine_id = :machineId AND is_active = 1")
    fun getMachineAccess(machineId: String): Flow<List<MachineAccessEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccess(access: MachineAccessEntity)

    @Query("UPDATE machine_access SET is_active = 0 WHERE id = :id")
    suspend fun revokeAccess(id: String)

    @Query("SELECT * FROM machine_access WHERE last_sync < :timestamp AND is_active = 1")
    suspend fun getAccessNeedingSync(timestamp: Long): List<MachineAccessEntity>
}