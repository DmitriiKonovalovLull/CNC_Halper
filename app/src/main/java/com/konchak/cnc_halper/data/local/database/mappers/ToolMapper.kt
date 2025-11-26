package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.domain.models.Tool

@Suppress("unused")
object ToolMapper {
    fun toEntity(domain: Tool): ToolEntity {
        return ToolEntity(
            id = 0, // id будет сгенерирован базой данных
            operatorId = domain.operatorId,
            name = domain.name,
            type = domain.type,
            size = domain.size,
            photoPath = domain.photoPath,
            createdAt = domain.createdAt
        )
    }

    fun toDomain(entity: ToolEntity): Tool {
        return Tool(
            id = entity.id.toString(),
            name = entity.name,
            type = entity.type,
            diameter = 0f, // diameter не хранится в ToolEntity
            length = 0f, // length не хранится в ToolEntity
            material = "", // material не хранится в ToolEntity
            wearLevel = 0, // wearLevel не хранится в ToolEntity
            lastUsed = 0, // lastUsed не хранится в ToolEntity
            machineId = "", // machineId не хранится в ToolEntity
            imagePath = entity.photoPath,
            isActive = true, // isActive не хранится в ToolEntity
            lastSync = 0, // lastSync не хранится в ToolEntity
            createdAt = entity.createdAt,
            operatorId = entity.operatorId,
            size = entity.size,
            photoPath = entity.photoPath
        )
    }

    fun toEntityList(domainList: List<Tool>): List<ToolEntity> {
        return domainList.map { toEntity(it) }
    }

    fun toDomainList(entityList: List<ToolEntity>): List<Tool> {
        return entityList.map { toDomain(it) }
    }
}