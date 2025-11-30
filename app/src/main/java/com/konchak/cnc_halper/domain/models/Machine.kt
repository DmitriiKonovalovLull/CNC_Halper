package com.konchak.cnc_halper.domain.models

data class Machine(
    val id: String,
    val name: String,
    val model: String,
    val serialNumber: String,
    val type: MachineType = MachineType.OTHER, // Changed to MachineType enum
    val isActive: Boolean = true,
    val lastSync: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val manufacturer: String,
    val year: Int,
    val status: String,
    val lastMaintenance: Long,
    val nextMaintenance: Long,
    val updatedAt: Long
) {
    val isWorking: Boolean
        get() = status == "Активен" || status == "В работе" // Assuming "Активен" or "В работе" means working

    companion object {
        fun createDefault(): Machine {
            return Machine(
                id = "",
                name = "",
                model = "",
                serialNumber = "",
                type = MachineType.OTHER, // Changed to MachineType enum
                lastSync = System.currentTimeMillis(),
                manufacturer = "",
                year = 0,
                status = "",
                lastMaintenance = 0,
                nextMaintenance = 0,
                updatedAt = 0
            )
        }
    }
}
