package com.konchak.cnc_halper.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.konchak.cnc_halper.presentation.navigation.AppNavigation
import com.konchak.cnc_halper.presentation.navigation.Screen

@Suppress("unused")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val screens = listOf(
        Screen.MachineList,
        Screen.ToolScanner,
        Screen.Chat,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.MachineList -> Icons.Default.Business
                                    Screen.ToolScanner -> Icons.Default.QrCodeScanner
                                    Screen.Chat -> Icons.AutoMirrored.Filled.Chat
                                    else -> Icons.Default.Person // Добавлена ветка else
                                },
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = when (screen) {
                                    Screen.MachineList -> "Станки"
                                    Screen.ToolScanner -> "Сканер"
                                    Screen.Chat -> "Чат"
                                    else -> "Профиль" // Добавлена ветка else
                                }
                            )
                        },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}