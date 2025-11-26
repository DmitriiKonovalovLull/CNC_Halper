package com.konchak.cnc_halper.domain.models.ui

data class MachineUiModel(
    val id: String,
    val name: String,
    val model: String,
    val serialNumber: String,
    val lastSync: Long = 0L // Добавлено для соответствия MachineListViewModel
)