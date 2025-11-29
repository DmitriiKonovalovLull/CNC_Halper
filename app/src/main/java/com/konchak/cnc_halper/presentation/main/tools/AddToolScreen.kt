// app/src/main/java/com/konchak/cnc_halper/app/src/main/java/com/konchak/cnc_halper/presentation/main/tools/AddToolScreen.kt
package com.konchak.cnc_halper.presentation.main.tools

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToolScreen(
    operatorId: Long,
    onNavigateBack: () -> Unit
) {
    var toolName by remember { mutableStateOf("") }
    var toolType by remember { mutableStateOf("") }
    var toolSize by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val viewModel: ToolViewModel = hiltViewModel()

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Фото успешно сохранено
        } else {
            // Обработка ошибки или отмены
        }
    }

    fun takePhoto() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(null)
        val photoFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )

        photoPath = photoFile.absolutePath

        val photoURI = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )

        cameraLauncher.launch(photoURI)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Добавить инструмент",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = toolName,
            onValueChange = { toolName = it },
            label = { Text("Название инструмента") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = toolType,
            onValueChange = { toolType = it },
            label = { Text("Тип инструмента") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = toolSize,
            onValueChange = { toolSize = it },
            label = { Text("Размер") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            singleLine = true
        )

        // Кнопка фото
        OutlinedButton(
            onClick = { takePhoto() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Сфоткать инструмент")
        }

        // Превью фото
        if (photoPath != null) {
            Text(
                text = "Фото сохранено: ${photoPath?.substringAfterLast("/")}",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка сохранения
        Button(
            onClick = {
                if (toolName.isNotBlank() && toolType.isNotBlank() && toolSize.isNotBlank()) {
                    viewModel.saveTool(
                        operatorId = operatorId,
                        name = toolName,
                        type = toolType,
                        size = toolSize,
                        photoPath = photoPath,
                        onSuccess = {
                            onNavigateBack()
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = toolName.isNotBlank() && toolType.isNotBlank() && toolSize.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Сохранить инструмент",
                    fontSize = 18.sp
                )
            }
        }

        // Показ ошибок
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

