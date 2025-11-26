package com.konchak.cnc_halper.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CNC Helper") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "CNC Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Добро пожаловать в CNC Helper",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Умный помощник оператора станков с ЧПУ. " +
                        "Анализ инструментов, рекомендации режимов и офлайн-работа.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    viewModel.onEvent(WelcomeEvent.StartOnboarding)
                    navController.navigate(Screen.RoleSelection.route)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Начать работу",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    viewModel.onEvent(WelcomeEvent.SkipOnboarding)
                    navController.navigate(Screen.Main.route)
                }
            ) {
                Text("Пропустить настройку")
            }
        }
    }
}