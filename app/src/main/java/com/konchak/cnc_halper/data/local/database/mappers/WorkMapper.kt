package com.konchak.cnc_halper.data.local.database.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.konchak.cnc_halper.data.local.database.entities.WorkEntity
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.domain.models.WorkStatus

object WorkMapper {
    private val gson = Gson()

    fun toEntity(domain: Work): WorkEntity {
        return WorkEntity(
            id = domain.id.toLongOrNull() ?: 0L,
            name = domain.name,
            description = domain.description,
            machineId = domain.machineId,
            toolIds = gson.toJson(domain.toolIds),
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status.name,
            operatorId = domain.operatorId
        )
    }

    fun toDomain(entity: WorkEntity): Work {
        val toolIdsType = object : TypeToken<List<String>>() {}.type
        val toolIds: List<String> = gson.fromJson(entity.toolIds, toolIdsType) ?: emptyList()

        return Work(
            id = entity.id.toString(),
            name = entity.name,
            description = entity.description,
            machineId = entity.machineId,
            toolIds = toolIds,
            startDate = entity.startDate,
            endDate = entity.endDate,
            status = WorkStatus.valueOf(entity.status),
            operatorId = entity.operatorId
        )
    }

    fun toEntityList(domainList: List<Work>): List<WorkEntity> {
        return domainList.map { toEntity(it) }
    }

    fun toDomainList(entityList: List<WorkEntity>): List<Work> {
        return entityList.map { toDomain(it) }
    }
}