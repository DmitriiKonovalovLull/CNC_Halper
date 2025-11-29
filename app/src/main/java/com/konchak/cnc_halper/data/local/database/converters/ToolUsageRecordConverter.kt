package com.konchak.cnc_halper.data.local.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.konchak.cnc_halper.domain.models.ToolUsageRecord

class ToolUsageRecordConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<ToolUsageRecord> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<ToolUsageRecord>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<ToolUsageRecord>): String {
        return gson.toJson(list)
    }
}