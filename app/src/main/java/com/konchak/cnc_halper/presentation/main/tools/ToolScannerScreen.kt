package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("unused")
@Composable
fun ToolScannerScreen(
    navController: NavController,
    viewModel: ToolScannerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    // ✅ ДОБАВЛЯЕМ: Обработка навигации
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            "tool_saved" -> {
                // Возвращаемся к списку инструментов
                navController.popBackStack()
                viewModel.clearNavigation()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Сканер инструментов") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ToolScannerEvent.ToggleFlash) }) {
                        Text(
                            text = if (state.isFlashOn) "💡" else "🔦",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.hasCameraPermission -> {
                    CameraPreviewSection(
                        state = state,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.weight(1f)
                    )
                }

                state.shouldShowPermissionRationale -> {
                    PermissionRationale(
                        onRequestPermission = { viewModel.onEvent(ToolScannerEvent.RequestPermission) },
                        modifier = Modifier.weight(1f)
                    )
                }

                else -> {
                    PermissionRequired(
                        onRequestPermission = { viewModel.onEvent(ToolScannerEvent.RequestPermission) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Результаты сканирования
            if (state.scanResult != null) {
                ScanResultCard(
                    scanResult = state.scanResult!!,
                    onAnalyze = { viewModel.onEvent(ToolScannerEvent.AnalyzeTool) },
                    onSave = { viewModel.onEvent(ToolScannerEvent.SaveTool) },
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Управление камерой
            CameraControls(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        
        // ✅ ДОБАВЛЯЕМ: Уведомление об успешном сохранении
        if (state.isSaved) {
            LaunchedEffect(Unit) {
                // Можно показать Snackbar или другое уведомление
            }
        }
    }
}

@Composable
fun CameraPreviewSection(
    state: ToolScannerState,
    @Suppress("unused") onEvent: (ToolScannerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Здесь будет Preview камеры
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (state.isAnalyzing) {
                CircularProgressIndicator()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📷",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "Наведите камеру на инструмент",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Перекрестие для наведения
        if (!state.isAnalyzing) {
            Text(
                text = "➕",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun CameraControls(
    state: ToolScannerState,
    onEvent: (ToolScannerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка выбора из галереи
        IconButton(
            onClick = { onEvent(ToolScannerEvent.PickFromGallery) },
            enabled = !state.isAnalyzing
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🖼️", style = MaterialTheme.typography.headlineSmall)
                Text("Галерея", style = MaterialTheme.typography.labelSmall)
            }
        }

        // Кнопка съемки
        IconButton(
            onClick = { onEvent(ToolScannerEvent.CaptureImage) },
            enabled = !state.isAnalyzing
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "📸",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Кнопка настроек
        IconButton(
            onClick = { onEvent(ToolScannerEvent.OpenSettings) },
            enabled = !state.isAnalyzing
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⚙️", style = MaterialTheme.typography.headlineSmall)
                Text("Настройки", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ScanResultCard(
    scanResult: ScanResult,
    onAnalyze: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📊 Результат сканирования",
                style = MaterialTheme.typography.titleMedium
            )

            // Предпросмотр изображения
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🔧", style = MaterialTheme.typography.headlineMedium)
                    Text("Изображение инструмента", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Информация о сканировании
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("✅ Качество:", style = MaterialTheme.typography.bodyMedium)
                Text("${scanResult.quality}%", style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("📏 Размер:", style = MaterialTheme.typography.bodyMedium)
                Text(scanResult.imageSize, style = MaterialTheme.typography.bodyMedium)
            }

            // Действия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onAnalyze,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("🤖 Анализировать ИИ")
                }

                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("💾 Сохранить")
                }
            }
        }
    }
}

@Composable
fun PermissionRationale(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📷",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Доступ к камере",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Для сканирования инструментов необходимо разрешение на использование камеры",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRequestPermission) {
            Text("✅ Предоставить доступ")
        }
    }
}

@Composable
fun PermissionRequired(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📷",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Камера не доступна",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Для работы сканера требуется доступ к камере",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRequestPermission) {
            Text("🔓 Запросить доступ")
        }
    }
}