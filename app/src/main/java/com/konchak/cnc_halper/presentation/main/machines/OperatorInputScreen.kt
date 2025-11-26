// app/src/main/java/com/konchak/cnc_halper/presentation/main/machines/OperatorInputScreen.kt
package com.konchak.cnc_halper.presentation.main.machines

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorInputScreen(
    onNavigateToChat: (Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var workshop by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val viewModel: OperatorViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Введите данные оператора",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя оператора") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = workshop,
            onValueChange = { workshop = it },
            label = { Text("Название цеха") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            singleLine = true
        )

        Button(
            onClick = {
                if (name.isNotBlank() && workshop.isNotBlank()) {
                    isLoading = true
                    viewModel.saveOperator(
                        name = name,
                        workshop = workshop,
                        onSuccess = { operatorId ->
                            isLoading = false
                            onNavigateToChat(operatorId)
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = name.isNotBlank() && workshop.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Продолжить",
                    fontSize = 18.sp
                )
            }
        }
    }
}