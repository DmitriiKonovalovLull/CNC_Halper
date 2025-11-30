package com.konchak.cnc_halper.data.database

import androidx.room.*
import com.konchak.cnc_halper.data.models.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Project>>

    @Insert
    suspend fun insert(project: Project)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getById(id: String): Project?
}
