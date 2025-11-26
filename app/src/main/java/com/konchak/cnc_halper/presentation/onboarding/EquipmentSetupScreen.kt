package com.konchak.cnc_halper.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.ui.MachineUiModel
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSetupScreen(
    navController: NavController,
    viewModel: EquipmentSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройка оборудования") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.machines.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.onEvent(EquipmentSetupEvent.CompleteSetup)
                        navController.navigate(Screen.Tutorial.route) {
                            // Очищаем back stack чтобы нельзя было вернуться к onboarding
                            popUpTo(Screen.EquipmentSetup.route) {
                                inclusive = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Done, "Готово") },
                    text = { Text("Завершить настройку") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Добавьте ваши станки",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Приложение будет синхронизировать данные по выбранным станкам",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                AddMachineForm(
                    state = state.addMachineState,
                    onEvent = viewModel::onEvent
                )
            }

            // Список добавленных станков
            items(state.machines.size) { index ->
                MachineCard(
                    machine = state.machines[index],
                    onDelete = {
                        viewModel.onEvent(EquipmentSetupEvent.RemoveMachine(state.machines[index].id))
                    }
                )
            }

            // Добавляем отступ внизу для FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AddMachineForm(
    state: AddMachineState,
    onEvent: (EquipmentSetupEvent) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Добавить станок",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(EquipmentSetupEvent.AddMachineNameChanged(it)) },
                label = { Text("Название станка") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.nameError != null
            )

            if (state.nameError != null) {
                Text(
                    text = state.nameError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            OutlinedTextField(
                value = state.model,
                onValueChange = { onEvent(EquipmentSetupEvent.AddMachineModelChanged(it)) },
                label = { Text("Модель") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.serialNumber,
                onValueChange = { onEvent(EquipmentSetupEvent.AddMachineSerialNumberChanged(it)) },
                label = { Text("Серийный номер") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = { onEvent(EquipmentSetupEvent.SubmitAddMachine) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Добавить станок")
                }
            }
        }
    }
}

@Composable
fun MachineCard(
    machine: MachineUiModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = machine.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (machine.model.isNotEmpty() || machine.serialNumber.isNotEmpty()) {
                    Text(
                        text = buildString {
                            if (machine.model.isNotEmpty()) {
                                append(machine.model)
                            }
                            if (machine.model.isNotEmpty() && machine.serialNumber.isNotEmpty()) {
                                append(" • ")
                            }
                            if (machine.serialNumber.isNotEmpty()) {
                                append(machine.serialNumber)
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Удалить", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}