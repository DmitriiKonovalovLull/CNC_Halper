package com.konchak.cnc_halper.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    navController: NavController,
    viewModel: RoleSelectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Выбор роли") },
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
                .padding(24.dp)
        ) {
            Text(
                text = "Кто вы?",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Выберите вашу роль для персонализации функций приложения",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoleCard(
                    title = "Оператор станка",
                    description = "Работа с инструментами, анализ износа, ежедневные операции",
                    icon = Icons.Default.PrecisionManufacturing,
                    isSelected = state.selectedRole == UserRole.OPERATOR,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.OPERATOR)) }
                )

                RoleCard(
                    title = "Технолог",
                    description = "Настройка режимов, оптимизация процессов, анализ эффективности",
                    icon = Icons.Default.Engineering,
                    isSelected = state.selectedRole == UserRole.TECHNOLOGIST,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.TECHNOLOGIST)) }
                )

                RoleCard(
                    title = "Мастер цеха",
                    description = "Управление оборудованием, планирование, контроль качества",
                    icon = Icons.Default.SupervisorAccount,
                    isSelected = state.selectedRole == UserRole.WORKSHOP_MASTER,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.WORKSHOP_MASTER)) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onEvent(RoleSelectionEvent.Continue)
                    navController.navigate(Screen.EquipmentSetup.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.selectedRole != null && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Продолжить",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}