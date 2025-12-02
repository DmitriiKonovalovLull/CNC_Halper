package com.konchak.cnc_halper.presentation.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.presentation.navigation.Screen

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val operatorState by viewModel.operatorState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ProfileHeader(
                operator = operatorState.operator,
                onEditClick = { onNavigate(Screen.EditProfile.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            ProfileMenuItem(
                icon = Icons.Default.ModelTraining,
                title = "Управление знаниями",
                onClick = { onNavigate("knowledge_management") }
            )
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.Help,
                title = "Помощь",
                onClick = { /* TODO */ }
            )
            ProfileMenuItem(
                icon = Icons.Default.UploadFile,
                title = "Экспорт знаний",
                onClick = { viewModel.exportKnowledge() }
            )
        }
    }
}

@Composable
private fun ProfileHeader(operator: Operator?, onEditClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = operator?.name?.take(2)?.uppercase() ?: "ОП",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = operator?.name ?: "Оператор",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = operator?.role?.russianName ?: "Роль не указана",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Редактировать профиль")
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
    }
}
