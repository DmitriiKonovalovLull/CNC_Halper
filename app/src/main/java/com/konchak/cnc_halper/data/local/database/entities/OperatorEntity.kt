// app/src/main/java/com/konchak/cnc_halper/data/local/database/entities/OperatorEntity.kt
package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "operators")
data class OperatorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "workshop")
    val workshop: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)