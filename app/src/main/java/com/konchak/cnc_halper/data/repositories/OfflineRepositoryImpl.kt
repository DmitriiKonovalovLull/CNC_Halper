package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.OfflineCacheDao
import com.konchak.cnc_halper.data.local.database.mappers.OfflineCacheMapper
import com.konchak.cnc_halper.domain.models.OfflineCacheItem
import com.konchak.cnc_halper.domain.repositories.OfflineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineRepositoryImpl @Inject constructor(
    private val offlineCacheDao: OfflineCacheDao
) : OfflineRepository {

    override suspend fun cacheData(item: OfflineCacheItem) {
        val entity = OfflineCacheMapper.toEntity(item)
        offlineCacheDao.insertCacheItem(entity)
    }

    override suspend fun getCachedData(type: String): List<OfflineCacheItem> {
        return offlineCacheDao.getCacheItemsByType(type).first().map { OfflineCacheMapper.toDomain(it) }
    }

    override suspend fun deleteCachedData(id: String) {
        offlineCacheDao.deleteCacheItem(id)
    }

    override suspend fun clearExpiredCache() {
        val monthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        offlineCacheDao.cleanupSyncedItems(monthAgo)
    }

    override suspend fun syncOfflineData() {
        // TODO: Implement actual sync logic
    }

    override fun getUnsyncedItems(): Flow<List<OfflineCacheItem>> {
        return offlineCacheDao.getUnsyncedItems().map { entities ->
            entities.map { OfflineCacheMapper.toDomain(it) }
        }
    }

    override suspend fun markItemAsSynced(id: String) {
        offlineCacheDao.markAsSynced(id)
    }

    override suspend fun getPendingSyncCount(): Int {
        return offlineCacheDao.getUnsyncedItems().first().size
    }
}