package com.konchak.cnc_halper.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.presentation.viewmodels.ToolsViewModel

@Composable
fun AIChatScreen(viewModel: ToolsViewModel = hiltViewModel()) {
    var message by remember { mutableStateOf("") }
    val aiResponse by viewModel.aiResponse.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        // История чата
        LazyColumn(modifier = Modifier.weight(1f)) {
            // ... сообщения
        }
        
        // Ввод сообщения
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Спроси о инструментах или станках...") }
            )
            Button(onClick = {
                viewModel.askAI(message)
                message = ""
            }) {
                Text("Спросить")
            }
        }
        
        // Ответ AI
        aiResponse?.let { response ->
            Text("AI: $response")
        }
    }
}
