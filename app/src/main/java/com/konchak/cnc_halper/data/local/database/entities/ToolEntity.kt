// app/src/main/java/com/konchak/cnc_halper/data/local/database/entities/ToolEntity.kt
package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "tools")
data class ToolEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "operator_id")
    val operatorId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "size")
    val size: String,

    @ColumnInfo(name = "photo_path")
    val photoPath: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)