package com.konchak.cnc_halper.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.presentation.components.SearchView
import com.konchak.cnc_halper.presentation.components.ToolItem
import com.konchak.cnc_halper.presentation.viewmodels.ToolsViewModel

@Composable
fun ToolsListScreen(
    viewModel: ToolsViewModel = hiltViewModel(),
    onAddTool: () -> Unit,
    onEditTool: (String) -> Unit
) {
    val tools by viewModel.tools.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Поиск
        SearchView(
            query = searchQuery,
            onQueryChange = { viewModel.searchTools(it) }
        )
        
        // Список
        LazyColumn {
            items(tools) { tool ->
                ToolItem(
                    tool = tool,
                    onEditClick = { onEditTool(tool.id) },
                    onDeleteClick = { viewModel.deleteTool(tool) }
                )
            }
        }
    }
    
    // FAB для добавления
    FloatingActionButton(
        onClick = onAddTool
    ) {
        Icon(Icons.Default.Add, "Добавить инструмент")
    }
}
