package com.konchak.cnc_halper.data.database

import androidx.room.*
import com.konchak.cnc_halper.data.models.Machine
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {
    @Query("SELECT * FROM machines")
    fun getAll(): Flow<List<Machine>>

    @Query("SELECT * FROM machines WHERE model LIKE '%' || :query || '%' OR manufacturer LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Machine>>

    @Insert
    suspend fun insert(machine: Machine)

    @Update
    suspend fun update(machine: Machine)

    @Delete
    suspend fun delete(machine: Machine)

    @Query("SELECT * FROM machines WHERE id = :id")
    suspend fun getById(id: String): Machine?
}
