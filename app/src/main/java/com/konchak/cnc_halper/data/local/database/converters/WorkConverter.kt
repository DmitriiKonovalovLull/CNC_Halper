package com.konchak.cnc_halper.data.local.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.konchak.cnc_halper.domain.models.WorkStatus

class WorkConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromToolIdsList(toolIds: List<String>): String {
        return gson.toJson(toolIds)
    }

    @TypeConverter
    fun toToolIdsList(toolIdsString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(toolIdsString, type)
    }

    @TypeConverter
    fun fromWorkStatus(status: WorkStatus): String {
        return status.name
    }

    @TypeConverter
    fun toWorkStatus(statusString: String): WorkStatus {
        return WorkStatus.valueOf(statusString)
    }
}