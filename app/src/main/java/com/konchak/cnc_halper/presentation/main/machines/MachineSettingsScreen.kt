package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.MachineType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineSettingsScreen(
    navController: NavController,
    machineId: String?,
    viewModel: MachineSettingsViewModel = hiltViewModel()
) {
    val machine by viewModel.machine.collectAsState()
    val latheSpindleSpeed by viewModel.latheSpindleSpeed.collectAsState()
    val millingSpindleSpeed by viewModel.millingSpindleSpeed.collectAsState()
    val generalSpindleSpeed by viewModel.generalSpindleSpeed.collectAsState()

    LaunchedEffect(machineId) {
        if (machineId != null) {
            viewModel.loadMachine(machineId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Настройки станка ${machine?.name ?: (machineId?.take(4) ?: "")}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (machine == null) {
                Text("Загрузка настроек станка...", style = MaterialTheme.typography.titleMedium)
                CircularProgressIndicator()
            } else {
                Text(
                    "Настройки для станка: ${machine?.name}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (machine?.type) {
                    MachineType.LATHE_MILLING -> {
                        OutlinedTextField(
                            value = latheSpindleSpeed,
                            onValueChange = { viewModel.onLatheSpindleSpeedChanged(it) },
                            label = { Text("Частота вращения токарного патрона") },
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("об/мин") }
                        )
                        OutlinedTextField(
                            value = millingSpindleSpeed,
                            onValueChange = { viewModel.onMillingSpindleSpeedChanged(it) },
                            label = { Text("Частота вращения фрезерного шпинделя") },
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("об/мин") }
                        )
                    }
                    MachineType.LATHE -> {
                        OutlinedTextField(
                            value = latheSpindleSpeed,
                            onValueChange = { viewModel.onLatheSpindleSpeedChanged(it) },
                            label = { Text("Частота токарного шпинделя") },
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("об/мин") }
                        )
                    }
                    else -> {
                        OutlinedTextField(
                            value = generalSpindleSpeed,
                            onValueChange = { viewModel.onGeneralSpindleSpeedChanged(it) },
                            label = { Text("Частота вращения шпинделя") },
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("об/мин") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.saveSettings() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить настройки")
                }
            }
        }
    }
}