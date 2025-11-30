package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Embedded

@Entity(tableName = "operators")
data class OperatorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "workshop")
    val workshop: String,

    @ColumnInfo(name = "shift")
    val shift: String, // "day", "night", "mixed"

    @ColumnInfo(name = "experience")
    val experience: Int, // months

    @ColumnInfo(name = "role")
    val role: String, // "OPERATOR", "ENGINEER", "PROGRAMMER", "MASTER", "TECHNOLOGIST", "WORKSHOP_MASTER", "ADMIN"

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),

    @Embedded
    val stats: OperatorStatsEntity
)
