package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.OfflineCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineCacheDao {
    @Query("SELECT * FROM offline_cache WHERE is_synced = 0 ORDER BY created_at ASC")
    fun getUnsyncedItems(): Flow<List<OfflineCacheEntity>>

    @Query("SELECT * FROM offline_cache WHERE type = :type AND is_synced = 0")
    fun getUnsyncedItemsByType(type: String): Flow<List<OfflineCacheEntity>>

    @Insert
    suspend fun insertCacheItem(item: OfflineCacheEntity)

    @Query("UPDATE offline_cache SET is_synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("UPDATE offline_cache SET sync_attempts = sync_attempts + 1 WHERE id = :id")
    suspend fun incrementSyncAttempts(id: String)

    @Query("DELETE FROM offline_cache WHERE is_synced = 1 AND created_at < :timestamp")
    suspend fun cleanupSyncedItems(timestamp: Long)

    @Query("DELETE FROM offline_cache WHERE id = :id")
    suspend fun deleteItem(id: String)

    @Query("SELECT * FROM offline_cache WHERE type = :type")
    fun getCacheItemsByType(type: String): Flow<List<OfflineCacheEntity>>

    @Query("DELETE FROM offline_cache WHERE id = :id")
    suspend fun deleteCacheItem(id: String)
}