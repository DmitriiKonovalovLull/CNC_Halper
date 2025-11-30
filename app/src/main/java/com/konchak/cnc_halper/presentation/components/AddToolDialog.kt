package com.konchak.cnc_halper.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.konchak.cnc_halper.data.models.Tool
import java.util.UUID

@Composable
fun AddToolDialog(
    tool: Tool? = null,
    onDismiss: () -> Unit,
    onSave: (Tool) -> Unit
) {
    var toolNumber by remember { mutableStateOf(tool?.toolNumber ?: "") }
    var name by remember { mutableStateOf(tool?.name ?: "") }
    var diameter by remember { mutableStateOf(tool?.diameter?.toString() ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tool == null) "Добавить инструмент" else "Редактировать инструмент") },
        text = {
            Column {
                OutlinedTextField(
                    value = toolNumber,
                    onValueChange = { toolNumber = it },
                    label = { Text("Номер инструмента") }
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") }
                )
                OutlinedTextField(
                    value = diameter,
                    onValueChange = { diameter = it },
                    label = { Text("Диаметр (мм)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newTool = Tool(
                    id = tool?.id ?: UUID.randomUUID().toString(),
                    toolNumber = toolNumber,
                    name = name,
                    diameter = diameter.toDoubleOrNull() ?: 0.0,
                    material = "", // можно добавить больше полей
                    manufacturer = ""
                )
                onSave(newTool)
                onDismiss()
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
