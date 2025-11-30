package com.konchak.cnc_halper.domain.models

enum class ToolType(val displayName: String) {
    END_MILL("Концевая фреза"),
    DRILL("Сверло"),
    TAP("Метчик"),
    REAMER("Развертка"),
    TOOL_HOLDER("Державка"),
    LATHE_TOOL("Токарный резец"),
    OTHER("Другой");

    companion object {
        fun fromDisplayName(displayName: String): ToolType {
            return values().firstOrNull { it.displayName == displayName } ?: OTHER
        }
    }
}
