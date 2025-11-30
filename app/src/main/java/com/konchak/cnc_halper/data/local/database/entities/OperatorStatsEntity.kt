package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.ColumnInfo

data class OperatorStatsEntity(
    @ColumnInfo(name = "completed_operations")
    val completedOperations: Int = 0,
    @ColumnInfo(name = "tools_used")
    val toolsUsed: Int = 0,
    @ColumnInfo(name = "quality_rate")
    val qualityRate: Int = 0,
    @ColumnInfo(name = "time_saved")
    val timeSaved: Int = 0
)
