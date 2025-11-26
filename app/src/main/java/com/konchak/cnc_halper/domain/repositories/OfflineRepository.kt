// domain/repositories/OfflineRepository.kt
package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.OfflineCacheItem
import kotlinx.coroutines.flow.Flow

interface OfflineRepository {
    suspend fun cacheData(item: OfflineCacheItem)
    suspend fun getCachedData(type: String): List<OfflineCacheItem>
    suspend fun deleteCachedData(id: String)
    suspend fun clearExpiredCache()
    suspend fun syncOfflineData()
    fun getUnsyncedItems(): Flow<List<OfflineCacheItem>>
    suspend fun markItemAsSynced(id: String)
    suspend fun getPendingSyncCount(): Int
}