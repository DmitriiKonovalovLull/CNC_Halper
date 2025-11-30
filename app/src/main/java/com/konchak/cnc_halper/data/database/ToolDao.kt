package com.konchak.cnc_halper.data.database

import androidx.room.*
import com.konchak.cnc_halper.data.models.Tool
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {
    @Query("SELECT * FROM tools")
    fun getAll(): Flow<List<Tool>>
    
    @Query("SELECT * FROM tools WHERE toolNumber LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Tool>>
    
    @Insert
    suspend fun insert(tool: Tool)
    
    @Update
    suspend fun update(tool: Tool)
    
    @Delete
    suspend fun delete(tool: Tool)
    
    @Query("SELECT * FROM tools WHERE id = :id")
    suspend fun getById(id: String): Tool?
}
