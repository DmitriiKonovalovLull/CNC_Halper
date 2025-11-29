package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
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
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolUsageRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolDetailScreen(
    navController: NavHostController,
    toolId: String?,
    viewModel: ToolDetailViewModel = hiltViewModel()
) {
    val tool by viewModel.tool.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    // Загрузка инструмента при инициализации
    LaunchedEffect(toolId) {
        toolId?.let { viewModel.loadTool(it) }
    }

    // Обработка навигации
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { event ->
            when {
                event == "tool_deleted" -> {
                    navController.popBackStack()
                    viewModel.clearNavigation()
                }
                event.startsWith("end_operation/") -> {
                    navController.navigate(event)
                    viewModel.clearNavigation()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Детали инструмента",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        tool?.let {
                            navController.navigate("edit_tool/${it.id}")
                        }
                    }) {
                        Icon(Icons.Default.Edit, "Редактировать")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                isLoading -> LoadingState()
                errorMessage != null -> ErrorState(errorMessage!!) {
                    toolId?.let { viewModel.loadTool(it) }
                }
                tool != null -> ToolDetailContent(
                    tool = tool!!,
                    viewModel = viewModel,
                    navController = navController,
                    toolId = toolId ?: ""
                )
                else -> EmptyState()
            }
        }
    }
}

@Composable
private fun ToolDetailContent(
    tool: Tool,
    viewModel: ToolDetailViewModel,
    navController: NavHostController,
    toolId: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ToolHeaderSection(tool)
        ToolParametersSection(tool)
        WearStatusSection(tool)
        UsageHistorySection(tool.usageHistory)
        ActionsSection(
            tool = tool,
            viewModel = viewModel,
            navController = navController,
            toolId = toolId
        )
    }
}

@Composable
private fun ToolHeaderSection(tool: Tool) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = when (tool.wearLevel) {
                            1 -> Color.Green.copy(alpha = 0.2f)
                            2 -> Color.Green.copy(alpha = 0.2f)
                            3 -> Color.Yellow.copy(alpha = 0.2f)
                            4 -> Color(0xFFFFA500).copy(alpha = 0.2f)
                            5 -> Color.Red.copy(alpha = 0.2f)
                            else -> Color.Gray.copy(alpha = 0.2f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(tool.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(tool.type, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
                Text("Размер: ${tool.getSizeString()}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                Text("ID: ${tool.id}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ToolParametersSection(tool: Tool) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Параметры инструмента", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            ParameterRow("Диаметр", "${tool.diameter} мм")
            ParameterRow("Длина", "${tool.length} мм")
            ParameterRow("Материал", tool.material)
            ParameterRow("Покрытие", tool.coating)
            ParameterRow("Статус", when (tool.status) {
                "active" -> "Активный"
                "needs_replacement" -> "Требует замены"
                "worn" -> "Изношен"
                "broken" -> "Сломан"
                "maintenance" -> "На обслуживании"
                else -> tool.status
            })
            ParameterRow("Станок", tool.machineId ?: "Не назначен")
        }
    }
}

@Composable
private fun ParameterRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun WearStatusSection(tool: Tool) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (tool.wearLevel) {
                1 -> Color.Green.copy(alpha = 0.1f)
                2 -> Color.Green.copy(alpha = 0.1f)
                3 -> Color.Yellow.copy(alpha = 0.1f)
                4 -> Color(0xFFFFA500).copy(alpha = 0.1f)
                5 -> Color.Red.copy(alpha = 0.1f)
                else -> Color.Gray.copy(alpha = 0.1f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = when (tool.wearLevel) {
                        1 -> Color.Green
                        2 -> Color.Green
                        3 -> Color.Yellow
                        4 -> Color(0xFFFFA500)
                        5 -> Color.Red
                        else -> Color.Gray
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Статус износа",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Text(
                tool.getWearStatus(),
                fontWeight = FontWeight.Medium
            )

            LinearProgressIndicator(
                progress = { tool.wearLevel / 5f },
                modifier = Modifier.fillMaxWidth(),
                color = when (tool.wearLevel) {
                    1 -> Color.Green
                    2 -> Color.Green
                    3 -> Color.Yellow
                    4 -> Color(0xFFFFA500)
                    5 -> Color.Red
                    else -> Color.Gray
                },
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )

            Text(
                if (tool.isAvailable()) "✅ Готов к работе" else "❌ Требует внимания",
                fontWeight = FontWeight.Medium,
                color = if (tool.isAvailable()) Color.Green else Color.Red
            )
        }
    }
}

@Composable
private fun UsageHistorySection(usageHistory: List<ToolUsageRecord>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("История использования", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            if (usageHistory.isEmpty()) {
                Text("История использования пуста.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                usageHistory.sortedByDescending { it.timestamp }.take(5).forEach { record ->
                    UsageHistoryItem(record = record)
                }
                if (usageHistory.size > 5) {
                    Text("... и еще ${usageHistory.size - 5} записей",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun UsageHistoryItem(record: ToolUsageRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Станок: ${record.machineId}", fontWeight = FontWeight.Medium)
            Text(
                formatTimestamp(record.timestamp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            if (record.notes.isNotEmpty()) {
                Text(
                    "Заметки: ${record.notes}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${record.duration / 1000} сек", fontWeight = FontWeight.Medium)
            Text(
                "Износ: ${record.finalWearLevel}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        .format(Date(timestamp))
}

@Composable
private fun ActionsSection(
    tool: Tool,
    viewModel: ToolDetailViewModel,
    navController: NavHostController,
    toolId: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Действия", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            if (tool.isAvailable()) {
                Button(
                    onClick = {
                        viewModel.markToolUsed(toolId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отметить использование")
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.markForReplacement(toolId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Пометить для замены")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        navController.navigate("edit_tool/${tool.id}")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("✏️ Редактировать")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.deleteTool(toolId)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("🗑️ Удалить")
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Загрузка инструмента...")
    }
}

@Composable
private fun ErrorState(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ошибка загрузки", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(error, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Инструмент не найден", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Попробуйте выбрать другой инструмент", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}