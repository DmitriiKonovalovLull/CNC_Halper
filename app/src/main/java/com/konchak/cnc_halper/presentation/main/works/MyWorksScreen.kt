package com.konchak.cnc_halper.presentation.main.works

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.presentation.navigation.Screen // Import Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWorksScreen(
    navController: NavController,
    viewModel: MyWorksViewModel = hiltViewModel()
) {
    val works by viewModel.works.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои работы") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddWork.route) // Navigate to AddWorkScreen
            }) {
                Icon(Icons.Filled.Add, "Добавить новую работу")
            }
        }
    ) { paddingValues ->
        if (works.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("У вас пока нет работ. Нажмите '+' чтобы добавить новую.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(works) { work ->
                    WorkItem(work = work, onClick = {
                        // TODO: Navigate to WorkDetailScreen
                        println("Clicked on work: ${work.name}")
                    })
                }
            }
        }
    }
}

@Composable
fun WorkItem(work: Work, onClick: (Work) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(work) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = work.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Последнее изменение: ${formatDate(work.endDate ?: work.startDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (work.isGostProject) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ГОСТ: ${work.gostType ?: "Не указан"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}