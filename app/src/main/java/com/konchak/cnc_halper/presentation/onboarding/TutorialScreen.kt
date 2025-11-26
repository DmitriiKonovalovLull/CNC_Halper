package com.konchak.cnc_halper.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(
    navController: NavController,
    viewModel: TutorialViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Обучение") },
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
        ) {
            // Индикатор прогресса
            LinearProgressIndicator(
                progress = { (state.currentStep + 1) / state.totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )

            // Контент обучения
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (state.currentStep) {
                    0 -> TutorialStep1()
                    1 -> TutorialStep2()
                    2 -> TutorialStep3()
                    3 -> TutorialStep4()
                }
            }

            // Навигация
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { viewModel.onEvent(TutorialEvent.PreviousStep) },
                    enabled = state.currentStep > 0
                ) {
                    Text("Назад")
                }

                // Индикатор шагов
                Row {
                    repeat(state.totalSteps) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    color = if (index == state.currentStep) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .padding(4.dp)
                        )
                        if (index < state.totalSteps - 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }

                if (state.currentStep < state.totalSteps - 1) {
                    Button(
                        onClick = { viewModel.onEvent(TutorialEvent.NextStep) }
                    ) {
                        Text("Далее")
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.onEvent(TutorialEvent.CompleteTutorial)
                            navController.navigate(Screen.Main.route)
                        }
                    ) {
                        Text("Начать работу")
                    }
                }
            }
        }
    }
}

@Composable
fun TutorialStep1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = "Камера",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Сканирование инструментов",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Используйте камеру для сканирования инструментов. " +
                    "Приложение автоматически определит тип и состояние инструмента.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TutorialStep2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SmartToy,
            contentDescription = "ИИ",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "ИИ-анализ износа",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Наш искусственный интеллект анализирует изображение " +
                    "и определяет степень износа инструмента с точностью до 95%.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TutorialStep3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = "Офлайн",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Работа без интернета",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Мини-ИИ на телефоне работает даже без подключения к интернету. " +
                    "Все данные синхронизируются при появлении сети.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TutorialStep4() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = "Синхронизация",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Автоматическая синхронизация",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "При подключении к Wi-Fi и зарядке приложение автоматически " +
                    "синхронизирует данные и обновляет модели ИИ.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}