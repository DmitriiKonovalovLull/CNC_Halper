package com.konchak.cnc_halper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.konchak.cnc_halper.presentation.screens.AIChatScreen
import com.konchak.cnc_halper.presentation.screens.ToolsListScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "tools"
    ) {
        composable("tools") {
            ToolsListScreen(
                onAddTool = { navController.navigate("add_tool") },
                onEditTool = { toolId -> navController.navigate("edit_tool/$toolId") }
            )
        }
        composable("add_tool") {
            // Экран добавления
        }
        composable("edit_tool/{toolId}") { _ -> // Renamed backStackEntry to _
            // val toolId = backStackEntry.arguments?.getString("toolId") // Removed unused variable
            // Экран редактирования
        }
        composable("ai_chat") {
            AIChatScreen()
        }
    }
}
