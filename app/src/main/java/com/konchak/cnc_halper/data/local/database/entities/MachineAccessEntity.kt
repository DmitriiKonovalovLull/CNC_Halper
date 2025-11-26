package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "machine_access")
data class MachineAccessEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "machine_id")
    val machineId: String,

    @ColumnInfo(name = "access_level")
    val accessLevel: String,

    @ColumnInfo(name = "granted_at")
    val grantedAt: Long,

    @ColumnInfo(name = "expires_at")
    val expiresAt: Long?,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,

    @ColumnInfo(name = "last_sync")
    val lastSync: Long
)