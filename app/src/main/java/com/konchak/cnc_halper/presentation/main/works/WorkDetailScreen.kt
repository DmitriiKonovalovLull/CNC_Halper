package com.konchak.cnc_halper.presentation.main.works

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Work

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkDetailScreen(
    navController: NavController,
    viewModel: WorkDetailViewModel = hiltViewModel()
) {
    val work by viewModel.work.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(work?.name ?: "Детали работы") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            work?.let {
                item { DrawingCard(drawingUrl = it.drawingUrl) }
                item { GostStandardsCard(gostStandards = it.gostStandards) }
                item { OperatorNotesCard(notes = it.operatorNotes) }
                item { RecommendedToolsCard(toolIds = it.toolIds) }
                item { RecommendedMachinesCard(machineId = it.machineId) }
            }
        }
    }
}

@Composable
private fun DrawingCard(drawingUrl: String?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Чертеж", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (drawingUrl != null) {
                // TODO: Отображение чертежа (например, через Coil)
                Text("URL: $drawingUrl")
            } else {
                Text("Чертеж не прикреплен")
            }
        }
    }
}

@Composable
private fun GostStandardsCard(gostStandards: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ГОСТы", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (gostStandards.isNotEmpty()) {
                gostStandards.forEach { Text(it) }
            } else {
                Text("ГОСТы не указаны")
            }
        }
    }
}

@Composable
private fun OperatorNotesCard(notes: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Примечания оператора", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(if (notes.isNotBlank()) notes else "Заметок нет")
        }
    }
}

@Composable
private fun RecommendedToolsCard(toolIds: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Рекомендованные инструменты", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (toolIds.isNotEmpty()) {
                toolIds.forEach { Text("ID инструмента: $it") }
            } else {
                Text("Инструменты не рекомендованы")
            }
        }
    }
}

@Composable
private fun RecommendedMachinesCard(machineId: String?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Рекомендованный станок", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (machineId != null) {
                Text("ID станка: $machineId")
            } else {
                Text("Станок не рекомендован")
            }
        }
    }
}
