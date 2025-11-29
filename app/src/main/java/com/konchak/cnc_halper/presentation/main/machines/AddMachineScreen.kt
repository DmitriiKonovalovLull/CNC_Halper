package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // ✅ ИСПРАВЛЕНО
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.konchak.cnc_halper.domain.models.Machine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMachineScreen(
    onBack: () -> Unit,
    onSave: (Machine) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Добавить станок") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") // ✅ ИСПРАВЛЕНО
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
            Button(
                onClick = {
                    val newMachine = Machine(
                        id = "",
                        name = name,
                        model = model,
                        serialNumber = serialNumber,
                        status = "idle",
                        lastSync = 0L,
                        manufacturer = "",
                        year = 0,
                        lastMaintenance = 0L,
                        nextMaintenance = 0L,
                        updatedAt = 0L
                    )
                    onSave(newMachine)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}