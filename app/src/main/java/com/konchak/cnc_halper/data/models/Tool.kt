package com.konchak.cnc_halper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tools")
data class Tool(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val toolNumber: String,
    val name: String,
    val diameter: Double,
    val material: String,
    val manufacturer: String,
    val sourceUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
