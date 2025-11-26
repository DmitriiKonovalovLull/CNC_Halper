// data/local/database/entities/MachineEntity.kt
package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "machines")
data class MachineEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val model: String,
    val manufacturer: String,
    val year: Int,
    val status: String,
    val lastMaintenance: Long,
    val nextMaintenance: Long,
    val isSynced: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)