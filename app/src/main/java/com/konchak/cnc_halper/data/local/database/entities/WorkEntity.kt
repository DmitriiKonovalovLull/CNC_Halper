package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "works")
data class WorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val machineId: String?,
    val toolIds: String, // Сохраняем как JSON строку
    val startDate: Long,
    val endDate: Long?,
    val status: String, // Сохраняем как строку
    val operatorId: String
)