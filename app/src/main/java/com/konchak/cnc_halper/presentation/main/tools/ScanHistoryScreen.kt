package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.presentation.main.tools.ScanResult // Импортируем ScanResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanHistoryScreen(
    navController: NavController,
    viewModel: ScanHistoryViewModel = hiltViewModel()
) {
    val scanHistory by viewModel.scanHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История сканирования") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (scanHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("История сканирования пуста.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scanHistory) { scanResult ->
                    ScanHistoryItem(scanResult = scanResult)
                }
            }
        }
    }
}

@Composable
fun ScanHistoryItem(scanResult: ScanResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Качество: ${scanResult.quality}%")
            Text("Размер: ${scanResult.imageSize}")
            // Добавьте другие поля из ScanResult, которые хотите отобразить
        }
    }
}
