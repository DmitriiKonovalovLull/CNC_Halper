// app/src/main/java/com/konchak/cnc_halper/presentation/main/export/ExportScreen.kt
package com.konchak.cnc_halper.presentation.main.export

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExportScreen(
    operatorId: Long // Removed @Suppress("unused")
) {
    val viewModel: ExportViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Collect StateFlow values properly
    val operatorsCount by viewModel.operatorsCount.collectAsState()
    val chatResponsesCount by viewModel.chatResponsesCount.collectAsState()
    val toolsCount by viewModel.toolsCount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Экспорт данных",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Статистика данных
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Статистика данных:",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DataCounterItem(
                    label = "Операторы",
                    count = operatorsCount
                )

                DataCounterItem(
                    label = "Ответы чата",
                    count = chatResponsesCount
                )

                DataCounterItem(
                    label = "Инструменты",
                    count = toolsCount
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка экспорта
        Button(
            onClick = {
                scope.launch {
                    val csvData = viewModel.generateCSVData() // УБРАЛ operatorId
                    exportToCSV(context, csvData)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Экспорт в CSV",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun DataCounterItem(label: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Bold
        )
    }
}

private fun exportToCSV(context: Context, csvData: String) {
    try {
        // Создаем файл
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val fileName = "cnc_data_export_$timeStamp.csv"
        val file = File(context.getExternalFilesDir(null), fileName)

        // Записываем данные
        file.writeText(csvData, Charsets.UTF_8)

        // Создаем URI для sharing
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        // Запускаем sharing intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "Экспорт данных CNC Helper"
            )
        )

    } catch (e: Exception) {
        e.printStackTrace()
        // TODO: Показать ошибку пользователю
    }
}