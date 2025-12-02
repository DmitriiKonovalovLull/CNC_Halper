package com.konchak.cnc_halper.presentation.main.works

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Work
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkScreen(
    navController: NavController,
    viewModel: MyWorksViewModel
) {
    var workName by remember { mutableStateOf("") }
    var workDescription by remember { mutableStateOf("") }
    var isGostProject by remember { mutableStateOf(false) }
    var gostStandards by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Добавить новую работу") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = workName,
                onValueChange = { workName = it },
                label = { Text("Название чертежа") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = workDescription,
                onValueChange = { workDescription = it },
                label = { Text("Описание работы") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Проект по ГОСТу?")
                Switch(
                    checked = isGostProject,
                    onCheckedChange = { isGostProject = it }
                )
            }
            if (isGostProject) {
                OutlinedTextField(
                    value = gostStandards,
                    onValueChange = { gostStandards = it },
                    label = { Text("ГОСТы (через запятую)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    val newWork = Work(
                        id = UUID.randomUUID().toString(),
                        name = workName,
                        description = workDescription,
                        isGostProject = isGostProject,
                        gostStandards = if (isGostProject) gostStandards.split(",").map { it.trim() } else emptyList(),
                        startDate = System.currentTimeMillis()
                    )
                    viewModel.addWork(newWork)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = workName.isNotBlank() && workDescription.isNotBlank()
            ) {
                Text("Сохранить работу")
            }
        }
    }
}
