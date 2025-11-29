package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konchak.cnc_halper.presentation.main.tools.components.WearLevelSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndOperationScreen(
    navController: NavHostController,
    toolId: String?,
    viewModel: ToolDetailViewModel = hiltViewModel()
) {
    var wearLevel by remember { mutableIntStateOf(1) }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Завершение операции", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Оцените износ инструмента после операции:")
            WearLevelSlider(
                wearLevel = wearLevel,
                onWearLevelChange = { wearLevel = it }
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Примечания (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (toolId != null) {
                        // ✅ ИСПРАВЛЕНО: передаем toolId
                        viewModel.addUsageRecord(toolId, wearLevel, notes)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить и завершить")
            }
        }
    }
}