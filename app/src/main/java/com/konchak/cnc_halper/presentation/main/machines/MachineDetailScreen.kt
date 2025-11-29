package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // ✅ ИСПРАВЛЕНО
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Machine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDetailScreen(
    navController: NavController,
    machineId: String?,
    viewModel: MachineDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(machineId) {
        if (machineId != null) {
            viewModel.onEvent(MachineDetailEvent.LoadMachine(machineId))
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(state.machine?.name ?: "Детали станка") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") // ✅ ИСПРАВЛЕНО
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: Редактирование станка
                        },
                        enabled = state.machine != null
                    ) {
                        Icon(Icons.Default.Edit, "Редактировать")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.machine == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("❌", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Станок не найден",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            else -> {
                MachineDetailContent(
                    machine = state.machine!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun MachineDetailContent(
    machine: Machine,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок с иконкой
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🏭",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = machine.name,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = machine.model,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Основная информация
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Основная информация",
                    style = MaterialTheme.typography.titleMedium
                )

                InfoRow("Модель", machine.model)
                InfoRow("Тип", machine.type)
                if (machine.serialNumber.isNotBlank()) {
                    InfoRow("Серийный номер", machine.serialNumber)
                }
                InfoRow("Статус", if (machine.isActive) "✅ Активен" else "⏸️ Неактивен")
            }
        }

        // Даты и синхронизация
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Временные метки",
                    style = MaterialTheme.typography.titleMedium
                )

                InfoRow("Создан", machine.createdAt.toString())
                InfoRow("Последняя синхронизация", machine.lastSync.toString())
            }
        }

        // Действия
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Действия",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = { /* TODO: Инструменты станка */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔧 Управление инструментами")
                }

                OutlinedButton(
                    onClick = { /* TODO: Статистика */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📊 Просмотр статистики")
                }

                OutlinedButton(
                    onClick = { /* TODO: Настройки */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⚙️ Настройки станка")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}