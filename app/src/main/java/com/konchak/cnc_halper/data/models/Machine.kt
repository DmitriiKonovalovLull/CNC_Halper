package com.konchak.cnc_halper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "machines")
data class Machine(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val model: String,
    val manufacturer: String,
    val maxRPM: Int,
    val power: Double,
    val sourceUrl: String = ""
)
