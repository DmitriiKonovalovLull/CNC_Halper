package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.MachineEntity
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.models.MachineType

@Suppress("unused")
object MachineMapper {
    fun toEntity(domain: Machine): MachineEntity {
        return MachineEntity(
            id = domain.id,
            name = domain.name,
            model = domain.model,
            serialNumber = domain.serialNumber, // Mapped serialNumber
            type = domain.type.displayName, // Convert enum to String
            manufacturer = domain.manufacturer,
            year = domain.year,
            status = domain.status,
            isActive = domain.isActive, // Mapped isActive
            lastSync = domain.lastSync, // Mapped lastSync
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
            serialNumber = entity.serialNumber, // Mapped serialNumber
            type = MachineType.fromDisplayName(entity.type), // Convert String to enum
            isActive = entity.isActive, // Mapped isActive
            lastSync = entity.lastSync, // Mapped lastSync
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