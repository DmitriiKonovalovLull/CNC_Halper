package com.konchak.cnc_halper.data.local.database.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolUsageRecord

object ToolMapper {

    private val gson = Gson()

    fun toEntity(domain: Tool): ToolEntity {
        return ToolEntity(
            id = domain.id.toLongOrNull() ?: 0,
            operatorId = domain.operatorId.toLong(),
            name = domain.name,
            type = domain.type,
            size = domain.getSizeString(),
            photoPath = domain.imageUrl,
            diameter = domain.diameter,
            length = domain.length,
            material = domain.material,
            coating = domain.coating,
            wearLevel = domain.wearLevel,
            status = domain.status,
            lastUsed = domain.lastUsed,
            machineId = domain.machineId,
            notes = domain.notes,
            createdAt = domain.createdAt,
            lastSync = domain.lastSync,
            isSynced = false,
            usageHistory = gson.toJson(domain.usageHistory)
        )
    }

    fun toDomain(entity: ToolEntity): Tool {
        val usageHistoryType = object : TypeToken<List<ToolUsageRecord>>() {}.type
        val usageHistory: List<ToolUsageRecord> = gson.fromJson(entity.usageHistory, usageHistoryType) ?: emptyList()

        return Tool(
            id = entity.id.toString(),
            name = entity.name,
            type = entity.type,
            diameter = entity.diameter,
            length = entity.length,
            material = entity.material,
            coating = entity.coating,
            wearLevel = entity.wearLevel,
            lastUsed = entity.lastUsed,
            machineId = entity.machineId,
            operatorId = entity.operatorId.toString(),
            status = entity.status,
            createdAt = entity.createdAt,
            lastSync = entity.lastSync,
            imageUrl = entity.photoPath,
            notes = entity.notes,
            usageHistory = usageHistory
        )
    }

    @Suppress("unused")
    fun toEntityList(domainList: List<Tool>): List<ToolEntity> {
        return domainList.map { toEntity(it) }
    }

    @Suppress("unused")
    fun toDomainList(entityList: List<ToolEntity>): List<Tool> {
        return entityList.map { toDomain(it) }
    }

    @Suppress("unused")
    fun fromFormData(
        operatorId: Long,
        name: String,
        type: String,
        size: String,
        photoPath: String?,
        diameter: Float = 0f,
        length: Float = 0f,
        material: String = "",
        coating: String = ""
    ): ToolEntity {
        return ToolEntity(
            operatorId = operatorId,
            name = name,
            type = type,
            size = size,
            photoPath = photoPath,
            diameter = diameter,
            length = length,
            material = material,
            coating = coating,
            status = "active",
            wearLevel = 1,
            lastUsed = System.currentTimeMillis(),
            notes = ""
        )
    }
}