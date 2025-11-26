package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.OfflineCacheEntity
import com.konchak.cnc_halper.domain.models.OfflineCacheItem
import com.konchak.cnc_halper.domain.models.CacheType

object OfflineCacheMapper {
    fun toEntity(domain: OfflineCacheItem): OfflineCacheEntity {
        return OfflineCacheEntity(
            id = domain.id,
            type = domain.type.name,
            data = domain.data,
            createdAt = domain.createdAt,
            isSynced = domain.isSynced,
            syncAttempts = domain.syncAttempts
        )
    }

    fun toDomain(entity: OfflineCacheEntity): OfflineCacheItem {
        return OfflineCacheItem(
            id = entity.id,
            type = enumValueOf<CacheType>(entity.type),
            data = entity.data,
            createdAt = entity.createdAt,
            isSynced = entity.isSynced,
            syncAttempts = entity.syncAttempts
        )
    }
}