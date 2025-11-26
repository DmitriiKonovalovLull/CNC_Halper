package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.MachineAccessEntity
import com.konchak.cnc_halper.domain.models.MachineAccess
import com.konchak.cnc_halper.domain.models.AccessLevel

object MachineAccessMapper {
    fun toEntity(domain: MachineAccess): MachineAccessEntity {
        return MachineAccessEntity(
            id = domain.id,
            userId = domain.userId,
            machineId = domain.machineId,
            accessLevel = domain.accessLevel.name,
            grantedAt = domain.grantedAt,
            expiresAt = domain.expiresAt,
            isActive = domain.isActive,
            lastSync = domain.lastSync
        )
    }

    fun toDomain(entity: MachineAccessEntity): MachineAccess {
        return MachineAccess(
            id = entity.id,
            userId = entity.userId,
            machineId = entity.machineId,
            accessLevel = enumValueOf<AccessLevel>(entity.accessLevel),
            grantedAt = entity.grantedAt,
            expiresAt = entity.expiresAt,
            isActive = entity.isActive,
            lastSync = entity.lastSync
        )
    }
}