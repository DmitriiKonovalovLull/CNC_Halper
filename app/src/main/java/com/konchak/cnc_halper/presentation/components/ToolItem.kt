package com.konchak.cnc_halper.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon // Added
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konchak.cnc_halper.data.models.Tool

@Composable
fun ToolItem(
    tool: Tool,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Tool) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Номер: ${tool.toolNumber}")
                Text("Название: ${tool.name}")
                Text("Диаметр: ${tool.diameter} мм")
            }
            Row {
                IconButton(onClick = { onEditClick(tool.id) }) {
                    Icon(Icons.Default.Edit, "Редактировать")
                }
                IconButton(onClick = { onDeleteClick(tool) }) {
                    Icon(Icons.Default.Delete, "Удалить")
                }
            }
        }
    }
}
