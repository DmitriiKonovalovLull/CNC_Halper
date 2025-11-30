package com.konchak.cnc_halper.presentation.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.konchak.cnc_halper.presentation.navigation.Screen
import com.konchak.cnc_halper.presentation.navigation.appGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val screens = listOf(
        Screen.MachineList,
        Screen.ToolList,
        Screen.MyWorks,
        Screen.Chat,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer, // Use a slightly different background for the bar
                tonalElevation = 5.dp // Add some shadow
            ) {
                screens.forEach { screen ->
                    val selected = currentDestination?.route == screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.MachineList -> Icons.Default.Business
                                    Screen.ToolList -> Icons.Default.Build
                                    Screen.MyWorks -> Icons.AutoMirrored.Filled.List // Changed to AutoMirrored.Filled.List
                                    Screen.Chat -> Icons.AutoMirrored.Filled.Chat
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
                                    Screen.MyWorks -> "Работы"
                                    Screen.Chat -> "Чат"
                                    Screen.Profile -> "Профиль"
                                    else -> "Профиль"
                                }
                            )
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.MachineList.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideInHorizontally(animationSpec = tween(600)) { it / 2 } + fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(600)) { -it / 2 } + fadeOut(animationSpec = tween(600))
            },
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(600)) { -it / 2 } + fadeIn(animationSpec = tween(600))
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec = tween(600)) { it / 2 } + fadeOut(animationSpec = tween(600))
            }
        ) {
            appGraph(navController)
        }
    }
}
