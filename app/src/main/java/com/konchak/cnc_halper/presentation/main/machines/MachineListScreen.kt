package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konchak.cnc_halper.domain.models.Machine
import com.konchak.cnc_halper.presentation.components.RotatingGear
import com.konchak.cnc_halper.presentation.components.WorkingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineListScreen(
    navController: NavHostController,
    viewModel: MachineListViewModel = hiltViewModel()
) {
    val machines by viewModel.machines.collectAsState()
    
    // Временные данные для демонстрации анимаций
    val workingMachines = remember { mutableStateListOf("machine_1", "machine_3") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Мои станки",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Запускаем экран добавления нового станка
                    navController.navigate("add_machine") // Используйте ваш маршрут
                }
            ) {
                Icon(Icons.Default.Add, "Добавить станок")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(machines, key = { it.id }) { machine ->
                MachineItem(
                    machine = machine,
                    isWorking = workingMachines.contains(machine.id),
                    onClick = { 
                        navController.navigate(
                            "machine_detail/${machine.id}"
                        ) 
                    }
                )
            }
        }
    }
}

@Composable
fun MachineItem(
    machine: Machine,
    isWorking: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Анимации работы
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                WorkingIndicator(isWorking = isWorking)
                RotatingGear(isRotating = isWorking)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Информация о станке
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    machine.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    machine.model,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                
                if (isWorking) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "⚡ В работе",
                        color = Color(0xFFFF6B35),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Статус станка
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (isWorking) "Активен" else "Остановлен",
                    color = if (isWorking) Color(0xFF48BB78) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Text(
                    "ID: ${machine.id.take(8)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}