package com.konchak.cnc_halper.domain.models

enum class MachineType(val displayName: String) {
    LATHE("Токарный"),
    MILLING("Фрезерный"),
    DRILLING("Сверлильный"),
    LATHE_MILLING("Токарно-фрезерный"),
    OTHER("Другой");

    companion object {
        fun fromDisplayName(displayName: String): MachineType {
            return values().firstOrNull { it.displayName == displayName } ?: OTHER
        }
    }

    fun isToolCompatible(toolType: ToolType): Boolean {
        return when (this) {
            LATHE -> toolType == ToolType.LATHE_TOOL
            MILLING -> toolType == ToolType.END_MILL || toolType == ToolType.DRILL || toolType == ToolType.TAP || toolType == ToolType.REAMER || toolType == ToolType.TOOL_HOLDER
            DRILLING -> toolType == ToolType.DRILL || toolType == ToolType.TAP || toolType == ToolType.REAMER
            LATHE_MILLING -> toolType == ToolType.LATHE_TOOL || toolType == ToolType.END_MILL || toolType == ToolType.DRILL || toolType == ToolType.TAP || toolType == ToolType.REAMER || toolType == ToolType.TOOL_HOLDER
            OTHER -> true // For "Other" machine types, assume all tools are compatible or handle specifically
        }
    }
}
