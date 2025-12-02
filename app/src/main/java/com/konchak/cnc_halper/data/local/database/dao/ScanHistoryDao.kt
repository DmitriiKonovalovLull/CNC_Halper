package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.konchak.cnc_halper.data.local.database.entities.ScanHistoryEntity

@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(entity: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<ScanHistoryEntity>
}
