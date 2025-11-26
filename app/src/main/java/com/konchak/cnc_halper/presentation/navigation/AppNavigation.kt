// app/src/main/java/com/konchak/cnc_halper/presentation/navigation/AppNavigation.kt
package com.konchak.cnc_halper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.konchak.cnc_halper.presentation.main.MainScreen
import com.konchak.cnc_halper.presentation.main.chat.ChatScreen
import com.konchak.cnc_halper.presentation.main.machines.MachineDetailScreen
import com.konchak.cnc_halper.presentation.main.machines.MachineListScreen
import com.konchak.cnc_halper.presentation.main.tools.ToolScannerScreen
import com.konchak.cnc_halper.presentation.onboarding.EquipmentSetupScreen
import com.konchak.cnc_halper.presentation.onboarding.RoleSelectionScreen
import com.konchak.cnc_halper.presentation.onboarding.TutorialScreen
import com.konchak.cnc_halper.presentation.onboarding.WelcomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(navController = navController)
        }
        composable(Screen.EquipmentSetup.route) {
            EquipmentSetupScreen(navController = navController)
        }
        composable(Screen.Tutorial.route) {
            TutorialScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            MainScreen()
        }
        composable(Screen.MachineList.route) {
            MachineListScreen(navController = navController)
        }
        composable(
            Screen.MachineDetail.route,
            arguments = listOf(navArgument("machineId") { type = NavType.StringType })
        ) { backStackEntry ->
            MachineDetailScreen(
                navController = navController,
                machineId = backStackEntry.arguments?.getString("machineId")
            )
        }
        composable(Screen.ToolScanner.route) {
            ToolScannerScreen(navController = navController)
        }
        composable(Screen.Chat.route) {
            ChatScreen()
        }
        composable(Screen.Settings.route) {
            // SettingsScreen(navController = navController)
        }
    }
}
