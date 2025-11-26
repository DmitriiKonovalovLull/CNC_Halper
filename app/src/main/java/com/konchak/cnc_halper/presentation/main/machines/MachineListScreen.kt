package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.ui.MachineUiModel
import com.konchak.cnc_halper.presentation.navigation.Screen.EquipmentSetup
import com.konchak.cnc_halper.presentation.navigation.Screen.MachineDetail
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("unused")
@Composable
fun MachineListScreen(
    navController: NavController,
    viewModel: MachineListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои станки") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(MachineListEvent.Refresh) }) {
                        Icon(Icons.Default.Refresh, "Обновить")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(EquipmentSetup.route)
                },
                icon = { Icon(Icons.Default.Add, "Добавить") },
                text = { Text("Добавить станок") }
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

            state.machines.isEmpty() -> {
                EmptyMachinesState(
                    modifier = Modifier.padding(paddingValues),
                    onAddMachine = {
                        navController.navigate(EquipmentSetup.route)
                    }
                )
            }

            else -> {
                MachineListContent(
                    state = state,
                    modifier = Modifier.padding(paddingValues),
                    onMachineClick = { machineId: String ->
                        navController.navigate(MachineDetail.createRoute(machineId))
                    },
                    onSyncClick = { viewModel.onEvent(MachineListEvent.SyncMachines) }
                )
            }
        }
    }
}

@Suppress("unused")
@Composable
fun MachineListContent(
    state: MachineListState,
    modifier: Modifier = Modifier,
    onMachineClick: (String) -> Unit,
    onSyncClick: () -> Unit
) {
    Column(modifier = modifier) {
        // Статус синхронизации
        if (state.syncStatus != null) {
            SyncStatusCard(
                syncStatus = state.syncStatus,
                onSyncClick = onSyncClick,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Список станков
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.machines, key = { it.id }) { machine ->
                MachineCard(
                    machine = machine,
                    onClick = { onMachineClick(machine.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Suppress("unused")
@Composable
fun MachineCard(
    machine: MachineUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Заменяем иконку на эмодзи
            Text(
                text = "🏭", // Эмодзи фабрики/станка
                modifier = Modifier.size(40.dp),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = machine.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = machine.model,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (machine.serialNumber.isNotBlank()) {
                    Text(
                        text = "SN: ${machine.serialNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Заменяем стрелку на эмодзи
            Text(
                text = "➡️", // Эмодзи стрелки вправо
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Suppress("unused")
@Composable
fun SyncStatusCard(
    syncStatus: MachineSyncStatus, // Исправлено: используем MachineSyncStatus вместо SyncStatus
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = when (syncStatus) {
                is MachineSyncStatus.Success -> MaterialTheme.colorScheme.surface
                is MachineSyncStatus.Error -> MaterialTheme.colorScheme.errorContainer
                is MachineSyncStatus.Pending -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = when (syncStatus) {
                        is MachineSyncStatus.Success -> "✅ Синхронизировано"
                        is MachineSyncStatus.Error -> "❌ Ошибка синхронизации"
                        is MachineSyncStatus.Pending -> "🔄 Требуется синхронизация"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                when (syncStatus) {
                    is MachineSyncStatus.Success -> {
                        Text(
                            text = "${syncStatus.syncedCount} станков обновлено",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    is MachineSyncStatus.Error -> {
                        Text(
                            text = syncStatus.message,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    is MachineSyncStatus.Pending -> {
                        Text(
                            text = "${syncStatus.pendingCount} станков ждут синхронизации",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            if (syncStatus is MachineSyncStatus.Pending || syncStatus is MachineSyncStatus.Error) {
                TextButton(onClick = onSyncClick) {
                    Text("🔄 Синхронизировать")
                }
            }
        }
    }
}

@Composable
fun EmptyMachinesState(
    modifier: Modifier = Modifier,
    onAddMachine: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заменяем иконку на эмодзи
        Text(
            text = "🏭", // Эмодзи фабрики/станка
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Станки не добавлены",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Добавьте ваши станки для начала работы",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onAddMachine) {
            Text("➕ Добавить первый станок")
        }
    }
}