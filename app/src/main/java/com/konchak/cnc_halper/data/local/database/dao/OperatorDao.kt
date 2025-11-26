// app/src/main/java/com/konchak/cnc_halper/data/local/database/dao/OperatorDao.kt
package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.OperatorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperatorDao {

    @Query("SELECT * FROM operators ORDER BY created_at DESC")
    fun getAllOperators(): Flow<List<OperatorEntity>>

    @Query("SELECT * FROM operators WHERE id = :id")
    suspend fun getOperatorById(id: Long): OperatorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperator(operator: OperatorEntity): Long

    @Update
    suspend fun updateOperator(operator: OperatorEntity)

    @Query("DELETE FROM operators WHERE id = :id")
    suspend fun deleteOperator(id: Long)

    @Query("SELECT COUNT(*) FROM operators")
    suspend fun getOperatorCount(): Int
}