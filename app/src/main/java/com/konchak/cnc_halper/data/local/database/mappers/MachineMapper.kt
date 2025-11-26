package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.domain.models.Machine

@Suppress("unused")
object MachineMapper {
    fun toEntity(domain: Machine): MachineEntity {
        return MachineEntity(
            id = domain.id,
            name = domain.name,
            model = domain.model,
            type = domain.type,
            manufacturer = domain.manufacturer,
            year = domain.year,
            status = domain.status,
            lastMaintenance = domain.lastMaintenance,
            nextMaintenance = domain.nextMaintenance,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomain(entity: MachineEntity): Machine {
        return Machine(
            id = entity.id,
            name = entity.name,
            model = entity.model,
            serialNumber = "", // serialNumber не хранится в MachineEntity
            type = entity.type,
            isActive = true, // isActive не хранится в MachineEntity
            lastSync = 0, // lastSync не хранится в MachineEntity
            createdAt = entity.createdAt,
            manufacturer = entity.manufacturer,
            year = entity.year,
            status = entity.status,
            lastMaintenance = entity.lastMaintenance,
            nextMaintenance = entity.nextMaintenance,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntityList(domainList: List<Machine>): List<MachineEntity> {
        return domainList.map { toEntity(it) }
    }

    fun toDomainList(entityList: List<MachineEntity>): List<Machine> {
        return entityList.map { toDomain(it) }
    }
}