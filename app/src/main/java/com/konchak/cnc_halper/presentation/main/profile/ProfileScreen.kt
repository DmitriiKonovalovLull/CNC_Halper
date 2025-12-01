package com.konchak.cnc_halper.presentation.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.R
import com.konchak.cnc_halper.domain.models.EnergyMode
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.domain.models.SyncStatus
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val operatorState by viewModel.operatorState.collectAsState()
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль оператора") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { onNavigate(Screen.EditProfile.route) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать профиль")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            EnergyModeCard(batteryLevel, viewModel.currentEnergyMode)
            OperatorInfoCard(operatorState.operator)
            OperatorStatsCard(operatorState.operator)
            QuickActionsCard(
                onToolsClick = { onNavigate(Screen.ToolScanner.route) },
                onSyncClick = { viewModel.manualSync() },
                syncStatus = syncStatus
            )
            SettingsSecurityCard(
                onSettingsClick = { onNavigate(Screen.Settings.route) },
                onBiometricClick = { viewModel.toggleBiometricAuth() }
            )
            SocialLoginCard(
                onGoogleLoginClick = { /* TODO: Implement Google login */ },
                onVkLoginClick = { /* TODO: Implement VK login */ },
                onRegisterClick = { onNavigate(Screen.Registration.route) }
            )
            MessengerCard(
                onMessengerClick = { /* TODO: Navigate to new chat messenger */ }
            )
            AITrainingCard(
                onTrainAIClick = { onNavigate("knowledge_management") },
                onExportKnowledgeClick = { viewModel.exportKnowledge() }
            )
        }
    }
}

@Composable
private fun AITrainingCard(
    onTrainAIClick: () -> Unit,
    onExportKnowledgeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ОБУЧЕНИЕ ИИ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ActionButton(
                    icon = Icons.Default.ModelTraining,
                    text = "Управление\nзнаниями",
                    onClick = onTrainAIClick
                )
                ActionButton(
                    icon = Icons.Default.UploadFile,
                    text = "Экспорт\nзнаний",
                    onClick = onExportKnowledgeClick
                )
            }
        }
    }
}


@Composable
private fun EnergyModeCard(
    batteryLevel: Int,
    energyMode: EnergyMode
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (energyMode) {
                EnergyMode.ECONOMY -> Color(0xFFFFF9C4)
                EnergyMode.STANDARD -> Color(0xFFC8E6C9)
                EnergyMode.PERFORMANCE -> Color(0xFFB3E5FC)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (energyMode) {
                    EnergyMode.ECONOMY -> Icons.Default.BatteryAlert
                    EnergyMode.STANDARD -> Icons.Default.BatteryStd
                    EnergyMode.PERFORMANCE -> Icons.Default.BatteryFull
                },
                contentDescription = "Режим энергии",
                tint = when (energyMode) {
                    EnergyMode.ECONOMY -> Color(0xFFF57C00)
                    EnergyMode.STANDARD -> Color(0xFF388E3C)
                    EnergyMode.PERFORMANCE -> Color(0xFF1976D2)
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = when (energyMode) {
                        EnergyMode.ECONOMY -> "ЭКОНОМНЫЙ РЕЖИМ"
                        EnergyMode.STANDARD -> "СТАНДАРТНЫЙ РЕЖИМ"
                        EnergyMode.PERFORMANCE -> "ПОЛНЫЙ РЕЖИМ"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Заряд: $batteryLevel% • ${getEnergyModeDescription(energyMode)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OperatorInfoCard(operator: Operator?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = operator?.name?.take(2)?.uppercase() ?: "ОП",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = operator?.name ?: "Оператор не указан",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = operator?.role?.russianName ?: "Роль не указана", // Display the russianName of the UserRole enum
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Цех: ${operator?.workshop ?: "Не указан"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    icon = Icons.Default.Schedule,
                    text = operator?.shift ?: "Смена не указана"
                )
                InfoChip(
                    icon = Icons.Default.Work,
                    text = "Опыт: ${operator?.experience ?: 0} мес."
                )
            }
        }
    }
}

@Composable
private fun OperatorStatsCard(operator: Operator?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "СТАТИСТИКА РАБОТЫ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = operator?.stats?.completedOperations?.toString() ?: "0",
                    label = "Операций"
                )
                StatItem(
                    value = operator?.stats?.toolsUsed?.toString() ?: "0",
                    label = "Инструментов"
                )
                StatItem(
                    value = "${operator?.stats?.qualityRate ?: 0}%",
                    label = "Качество"
                )
                StatItem(
                    value = "${operator?.stats?.timeSaved ?: 0}ч",
                    label = "Сэкономлено"
                )
            }
        }
    }
}

@Composable
private fun QuickActionsCard(
    onToolsClick: () -> Unit,
    onSyncClick: () -> Unit,
    syncStatus: SyncStatus
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "БЫСТРЫЕ ДЕЙСТВИЯ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton(
                    icon = Icons.Default.QrCodeScanner,
                    text = "Сканер\nинструментов",
                    onClick = onToolsClick
                )

                ActionButton(
                    icon = if (syncStatus == SyncStatus.SYNCING) Icons.Default.Sync else Icons.Default.CloudSync,
                    text = when (syncStatus) {
                        SyncStatus.SYNCING -> "Синхр...\nзация"
                        SyncStatus.SUCCESS -> "Синхро\nнизировано"
                        SyncStatus.ERROR -> "Ошибка\nсинхр."
                        else -> "Синхро\nнизация"
                    },
                    onClick = onSyncClick
                )

                ActionButton(
                    icon = Icons.Default.Assistant,
                    text = "Помощник\nИИ",
                    onClick = { /* TODO */ }
                )

                ActionButton(
                    icon = Icons.Default.Assessment,
                    text = "Отчет\nза смену",
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun SettingsSecurityCard(
    onSettingsClick: () -> Unit,
    onBiometricClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "БЕЗОПАСНОСТЬ И НАСТРОЙКИ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton(
                    icon = Icons.Default.Settings,
                    text = "Настройки",
                    onClick = onSettingsClick
                )

                ActionButton(
                    icon = Icons.Default.Fingerprint,
                    text = "Биометрия",
                    onClick = onBiometricClick
                )

                ActionButton(
                    icon = Icons.Default.Security,
                    text = "Безопасность",
                    onClick = { /* TODO */ }
                )

                ActionButton(
                    icon = Icons.AutoMirrored.Filled.Help,
                    text = "Помощь",
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun SocialLoginCard(
    onGoogleLoginClick: () -> Unit,
    onVkLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ВХОД И РЕГИСТРАЦИЯ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton(
                    icon = painterResource(id = R.drawable.ic_google), // Используем иконку Google
                    text = "Войти\nчерез Google",
                    onClick = onGoogleLoginClick
                )

                ActionButton(
                    icon = painterResource(id = R.drawable.ic_vk), // Используем иконку VK
                    text = "Войти\nчерез VK",
                    onClick = onVkLoginClick
                )

                ActionButton(
                    icon = Icons.Default.PersonAdd,
                    text = "Регистрация",
                    onClick = onRegisterClick
                )
            }
        }
    }
}

@Composable
private fun MessengerCard(
    onMessengerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ЧАТ-МЕССЕНДЖЕР",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ActionButton(
                    icon = Icons.Default.MailOutline,
                    text = "Открыть\nМессенджер",
                    onClick = onMessengerClick
                )
            }
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ActionButton(
    icon: Any, // Изменено на Any для поддержки ImageVector и Painter
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
    ) {
        if (icon is ImageVector) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        } else if (icon is androidx.compose.ui.graphics.painter.Painter) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

private fun getEnergyModeDescription(mode: EnergyMode): String {
    return when (mode) {
        EnergyMode.ECONOMY -> "Только базовые функции"
        EnergyMode.STANDARD -> "Все основные функции"
        EnergyMode.PERFORMANCE -> "Все функции + ИИ"
    }
}
