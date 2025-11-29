// app/src/main/java/com/konchak/cnc_halper/presentation/main/MainScreen.kt
package com.konchak.cnc_halper.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat // ✅ ИСПРАВЛЕНО
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.konchak.cnc_halper.presentation.navigation.Screen
import com.konchak.cnc_halper.presentation.navigation.appGraph

@Composable
fun MainScreen() { // ✅ ИСПРАВЛЕНО: убрал неиспользуемый параметр viewModel
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val screens = listOf(
        Screen.MachineList,
        Screen.ToolList,
        Screen.Chat,
        Screen.Profile
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
                                    Screen.ToolList -> Icons.Default.Build
                                    Screen.Chat -> Icons.AutoMirrored.Filled.Chat // ✅ ИСПРАВЛЕНО
                                    Screen.Profile -> Icons.Default.Person
                                    else -> Icons.Default.Person
                                },
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = when (screen) {
                                    Screen.MachineList -> "Станки"
                                    Screen.ToolList -> "Инструменты"
                                    Screen.Chat -> "Чат"
                                    Screen.Profile -> "Профиль"
                                    else -> "Профиль"
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
        androidx.navigation.compose.NavHost(
            navController = navController,
            startDestination = Screen.MachineList.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            appGraph(navController)
        }
    }
}