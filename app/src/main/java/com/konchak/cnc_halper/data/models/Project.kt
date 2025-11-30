package com.konchak.cnc_halper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val machineId: String,
    val description: String,
    val status: String = "active",
    val createdAt: Long = System.currentTimeMillis()
)
