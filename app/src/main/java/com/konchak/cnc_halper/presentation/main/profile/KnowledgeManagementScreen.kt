package com.konchak.cnc_halper.presentation.main.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeManagementScreen(
    navController: NavController,
    viewModel: KnowledgeViewModel = hiltViewModel()
) {
    val knowledgeList by viewModel.knowledgeList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Управление знаниями ИИ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Add new knowledge */ }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить знание")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(knowledgeList) { knowledge ->
                KnowledgeItem(
                    knowledge = knowledge,
                    onEdit = { /* TODO: Edit knowledge */ },
                    onDelete = { viewModel.deleteKnowledge(knowledge) }
                )
            }
        }
    }
}

@Composable
private fun KnowledgeItem(
    knowledge: AIKnowledgeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Вопрос: ${knowledge.question}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ответ: ${knowledge.answer}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Категория: ${knowledge.category}", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
        }
    }
}
