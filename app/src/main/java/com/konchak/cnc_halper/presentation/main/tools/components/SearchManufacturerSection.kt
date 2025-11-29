package com.konchak.cnc_halper.presentation.main.tools.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.konchak.cnc_halper.domain.models.ManufacturerTool

@Composable
fun SearchManufacturerSection(
    searchQuery: String,
    searchResults: List<ManufacturerTool>,
    onSearchChanged: (String) -> Unit,
    onToolSelected: (ManufacturerTool) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Поиск по базам производителей",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                label = { Text("Введите название или артикул") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )
            AnimatedVisibility(visible = searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(top = 8.dp)
                ) {
                    items(searchResults) { tool ->
                        ListItem(
                            headlineContent = { Text(tool.name) },
                            supportingContent = { Text("${tool.manufacturer} - ${tool.code}") },
                            modifier = Modifier.clickable { onToolSelected(tool) }
                        )
                    }
                }
            }
        }
    }
}