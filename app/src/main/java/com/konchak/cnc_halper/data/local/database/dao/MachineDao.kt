// data/local/database/dao/MachineDao.kt
package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.domain.models.Machine
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {
    @Query("SELECT * FROM machines")
    fun getMachines(): Flow<List<MachineEntity>>

    @Query("SELECT * FROM machines WHERE id = :id")
    suspend fun getMachineById(id: String): MachineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachine(machine: MachineEntity)

    @Update
    suspend fun updateMachine(machine: MachineEntity)

    @Query("DELETE FROM machines WHERE id = :id")
    suspend fun deleteMachine(id: String)
}