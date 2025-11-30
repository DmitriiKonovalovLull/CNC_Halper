package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // Import Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.domain.models.MachineType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMachineScreen(
    onBack: () -> Unit,
    onSave: (Machine) -> Unit,
    machineId: String?, // Null for adding, not null for editing
    viewModel: MachineDetailViewModel = hiltViewModel() // Using MachineDetailViewModel to load machine data
) {
    val isEditing = machineId != null
    val title = if (isEditing) "Редактировать станок" else "Добавить станок"

    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var selectedMachineType by remember { mutableStateOf(MachineType.OTHER) }
    var manufacturer by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("idle") } // Default status
    var isActive by remember { mutableStateOf(true) } // Default isActive

    // Load machine data if in editing mode
    LaunchedEffect(machineId) {
        if (isEditing && machineId != null) {
            viewModel.onEvent(MachineDetailEvent.LoadMachine(machineId))
        }
    }

    val machineState by viewModel.state.collectAsState()

    LaunchedEffect(machineState.machine) {
        machineState.machine?.let { machine ->
            name = machine.name
            model = machine.model
            serialNumber = machine.serialNumber
            selectedMachineType = machine.type
            manufacturer = machine.manufacturer
            year = machine.year.toString()
            status = machine.status
            isActive = machine.isActive
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название станка") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Модель") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = serialNumber,
                onValueChange = { serialNumber = it },
                label = { Text("Серийный номер") },
                modifier = Modifier.fillMaxWidth()
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedMachineType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Тип станка") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    MachineType.values().forEach { machineType ->
                        DropdownMenuItem(
                            text = { Text(machineType.displayName) },
                            onClick = {
                                selectedMachineType = machineType
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = manufacturer,
                onValueChange = { manufacturer = it },
                label = { Text("Производитель") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год выпуска") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Активен:", modifier = Modifier.weight(1f))
                Switch(
                    checked = isActive,
                    onCheckedChange = { isActive = it }
                )
            }

            Button(
                onClick = {
                    val machineToSave = Machine(
                        id = machineId ?: UUID.randomUUID().toString(),
                        name = name,
                        model = model,
                        serialNumber = serialNumber,
                        type = selectedMachineType,
                        isActive = isActive,
                        lastSync = System.currentTimeMillis(),
                        manufacturer = manufacturer,
                        year = year.toIntOrNull() ?: 0,
                        status = status,
                        lastMaintenance = System.currentTimeMillis(),
                        nextMaintenance = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(machineToSave)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && model.isNotBlank() && serialNumber.isNotBlank()
            ) {
                Text(if (isEditing) "Обновить" else "Сохранить")
            }
        }
    }
}