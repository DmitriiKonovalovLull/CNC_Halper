package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.presentation.main.machines.MachineDetailEvent
import com.konchak.cnc_halper.presentation.main.machines.MachineDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolListForMachineScreen(
    navController: NavController,
    machineId: String?,
    toolListViewModel: ToolListViewModel = hiltViewModel(),
    machineDetailViewModel: MachineDetailViewModel = hiltViewModel()
) {
    val machineState by machineDetailViewModel.state.collectAsState()
    val allTools by toolListViewModel.tools.collectAsState()

    LaunchedEffect(machineId) {
        if (machineId != null) {
            machineDetailViewModel.onEvent(MachineDetailEvent.LoadMachine(machineId))
            toolListViewModel.loadTools() // Load all tools
        }
    }

    val compatibleTools = remember(machineState.machine, allTools) {
        val machine = machineState.machine
        if (machine != null) {
            allTools.filter { tool ->
                machine.type.isToolCompatible(tool.type)
            }
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Инструменты для ${machineState.machine?.name ?: "Станка"}",
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
        when {
            machineState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            machineState.machine == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Станок не найден.")
                }
            }
            compatibleTools.isEmpty() && !machineState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет подходящих инструментов для этого станка.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(compatibleTools, key = { it.id }) { tool ->
                        ToolItem(tool = tool) {
                            navController.navigate("tool_detail/${tool.id}")
                        }
                    }
                }
            }
        }
    }
}
